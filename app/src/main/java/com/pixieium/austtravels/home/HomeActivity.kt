package com.pixieium.austtravels.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.livetrack.LiveTrackActivity
import com.pixieium.austtravels.home.stopwatch.StopwatchHandler
import com.pixieium.austtravels.models.UserInfo
import com.pixieium.austtravels.routes.RoutesActivity
import com.pixieium.austtravels.volunteers.VolunteersActivity
import kotlinx.coroutines.launch
import android.content.*
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.pixieium.austtravels.BuildConfig
import com.pixieium.austtravels.home.services.ForegroundOnlyLocationService
import com.pixieium.austtravels.settings.SettingsActivity
import com.pixieium.austtravels.utils.Constant.MSG_START_TIMER
import com.pixieium.austtravels.utils.Constant.MSG_STOP_TIMER
import com.pixieium.austtravels.utils.Constant.REQUEST_DIRECTIONS
import com.pixieium.austtravels.utils.Constant.REQUEST_LIVE_TRACK
import com.pixieium.austtravels.utils.Constant.REQUEST_SHARE_LOCATION
import com.pixieium.austtravels.utils.SharedPreferenceUtil


class HomeActivity : AppCompatActivity(),
    ProminentDisclosureDialog.FragmentListener,
    SelectBusDialog.FragmentListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityHomeBinding
    private val mDatabase: HomeRepository = HomeRepository()

    private lateinit var mSelectedBusName: String
    private lateinit var mSelectedBusTime: String

    private lateinit var mUid: String
    private var mIsVolunteer: Boolean = false

    private lateinit var mStopwatchHandler: StopwatchHandler

    // location sharing variables
    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: HomeActivity.ForegroundOnlyBroadcastReceiver
    private lateinit var sharedPreferences: SharedPreferences

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUid = Firebase.auth.currentUser?.uid.toString()
        setSupportActionBar(binding.topAppBar)

        initLocationSharing()

        mStopwatchHandler = StopwatchHandler(binding.sharingYourLocation)

        val userInfo: UserInfo? = mDatabase.getUserInfo()
        if (userInfo != null) {
            binding.loggedInAs.text =
                getString(R.string.logged_in_as_s, userInfo.email)
            binding.profileImage.loadSvg(userInfo.userImage)
        }

        lifecycleScope.launch {
            mIsVolunteer = mDatabase.isVolunteer(mUid)

            Log.d("mUid  -> ", mUid)
            Log.d("isVolunteer", mDatabase.isVolunteer(mUid).toString())

            if (!mIsVolunteer) {
                binding.shareLocation.visibility = View.GONE

                // If we remove someone from volunteer list, then we will unsubscribe the user from bus notification
                val busName = mDatabase.busNameOfVolunteer(mUid);
                busName?.let {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(it).addOnSuccessListener {
                        Log.d("unsubscribeFromTopic -", busName)
                    }
                }

            } else {
                binding.shareLocation.visibility = View.VISIBLE
                // subscribe the user to bus notification
                val busName = mDatabase.busNameOfVolunteer(mUid);
                busName?.let {
                    FirebaseMessaging.getInstance().subscribeToTopic(it).addOnSuccessListener {
                        Toast.makeText(
                            this@HomeActivity,
                            "You will receive notifications about $busName",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        if (isLocationSharing) {
            binding.shareLocation.text = getString(R.string.stop_sharing_location)
            binding.cardView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.GONE
        }


    }

    override fun onStart() {
        super.onStart()

        updateUiForLocationSharing(
            sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        // Updates button states if new while in use location is added to SharedPreferences.
        if (key == SharedPreferenceUtil.KEY_FOREGROUND_ENABLED) {
            updateUiForLocationSharing(
                sharedPreferences.getBoolean(
                    SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
                )
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")
                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    // Permission was granted.
                    // open bus select dialog
                    SelectBusDialog.newInstance(REQUEST_SHARE_LOCATION)
                        .show(supportFragmentManager, SelectBusDialog.TAG)
                else -> {
                    // Permission denied.
                    updateUiForLocationSharing(false)

                    Snackbar.make(
                        binding.root,
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private fun updateUiForLocationSharing(trackingLocation: Boolean) {
        if (trackingLocation) {
            binding.shareLocation.text = getString(R.string.stop_sharing_location)
            binding.cardView.visibility = View.VISIBLE
            binding.busName.text = getString(R.string.bus_jamuna, mSelectedBusName)
            binding.busTime.text = getString(R.string.time_6_45_am, mSelectedBusTime)

            Toast.makeText(
                this@HomeActivity, "Started sharing location",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            binding.shareLocation.text = getString(R.string.share_location)
            binding.cardView.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBusSelectClick(
        selectedBusName: String,
        selectedBusTime: String,
        requestCode: Int
    ) {
        when (requestCode) {
            REQUEST_SHARE_LOCATION -> {
                mSelectedBusName = selectedBusName
                mSelectedBusTime = selectedBusTime
                Log.d(TAG, mSelectedBusName)
                Log.d(TAG, mSelectedBusTime)

                startLocationSharing()
            }
            REQUEST_LIVE_TRACK -> {
                val intent = Intent(this@HomeActivity, LiveTrackActivity::class.java)
                intent.putExtra("SELECTED_BUS_NAME", selectedBusName)
                intent.putExtra("SELECTED_BUS_TIME", selectedBusTime)
                startActivity(intent)
            }
        }

    }

    private fun startLocationSharing() {
        updateUiForLocationSharing(true)
        foregroundOnlyLocationService?.subscribeToLocationUpdates()
            ?: Log.d(TAG, "Service Not Bound")
        startStopwatch()
        Toast.makeText(
            this@HomeActivity,
            "Location sharing started. Yay! Keep it going",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun stopLocationSharing() {
        updateUiForLocationSharing(false)
        stopStopwatch()
        foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
        Toast.makeText(
            this@HomeActivity,
            "Location sharing turned off. Thank you for your contribution!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onLiveTrackBusClick(view: View) {
        SelectBusDialog.newInstance(REQUEST_LIVE_TRACK)
            .show(supportFragmentManager, SelectBusDialog.TAG)
    }

    fun onViewDirectionsClick(view: View) {
        SelectBusDialog.newInstance(REQUEST_DIRECTIONS)
            .show(supportFragmentManager, SelectBusDialog.TAG)
    }

    fun onViewRoutesClick(view: View) {
        val intent = Intent(this@HomeActivity, RoutesActivity::class.java)
        startActivity(intent)
    }

    fun onShareLocationClick(view: View) {
        val isLocationSharing = sharedPreferences.getBoolean(
            SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false
        )

        if (isLocationSharing) {
            // if foreground location is already being shared, then stop it
            stopLocationSharing()
        } else {
            if (foregroundPermissionApproved()) {
                // open dialog to select bus
                SelectBusDialog.newInstance(REQUEST_SHARE_LOCATION)
                    .show(supportFragmentManager, SelectBusDialog.TAG)
            } else {
                // show prominent disclosure
                ProminentDisclosureDialog.newInstance()
                    .show(supportFragmentManager, ProminentDisclosureDialog.TAG)
            }
        }

    }


    fun onViewVolunteersClick(view: View) {
        // Toast.makeText(this, "volunteer", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@HomeActivity, VolunteersActivity::class.java)
        startActivity(intent)
    }

    override fun onDisclosureAcceptClick() {
        Toast.makeText(
            this@HomeActivity, "Requires location permission",
            Toast.LENGTH_SHORT
        ).show()

        // after user accepts agreement, request foreground permissions if not given already
        if (!foregroundPermissionApproved()) {
            requestForegroundPermissions()
        }
    }

    private fun startStopwatch() {
        mStopwatchHandler.sendEmptyMessage(MSG_START_TIMER)
    }

    private fun stopStopwatch() {
        mStopwatchHandler.sendEmptyMessage(MSG_STOP_TIMER)
    }

    /**
     * By default, ImageViews don't support SVG formats.
     * So, instead we are using the coil library to render svg files
     */
    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(2)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    private fun initLocationSharing() {
        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                binding.root,
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this@HomeActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d(TAG, "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    /**
     * Receiver for location broadcasts from [ForegroundOnlyLocationService].
     */
    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                mDatabase.updateLocation(mUid, mSelectedBusName, mSelectedBusTime, location)
            }
        }
    }

    companion object {
        private var isLocationSharing = false
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val TAG = "HomeActivity"
    }
}


