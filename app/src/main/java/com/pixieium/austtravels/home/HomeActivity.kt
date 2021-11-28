package com.pixieium.austtravels.home

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.livetrack.LiveTrackActivity
import com.pixieium.austtravels.home.stopwatch.StopwatchHandler
import com.pixieium.austtravels.models.UserInfo
import com.pixieium.austtravels.routes.RoutesActivity
import com.pixieium.austtravels.volunteers.VolunteersActivity
import kotlinx.coroutines.launch
import android.app.PendingIntent

import android.content.SharedPreferences


class HomeActivity : AppCompatActivity(), PromptVolunteerDialog.FragmentListener,
    ProminentDisclosureDialog.FragmentListener,
    SelectBusDialog.FragmentListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mLocationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private val mDatabase: HomeRepository = HomeRepository()
    private val REQUEST_LIVE_TRACK = 0
    private val REQUEST_DIRECTIONS = 1
    private val REQUEST_SHARE_LOCATION = 2
    private val MSG_START_TIMER = 0
    private val MSG_STOP_TIMER = 1
    private val MSG_UPDATE_TIMER = 2
    private val NOTIFICATION_ID = 12044

    private lateinit var mSelectedBusName: String
    private lateinit var mSelectedBusTime: String
    private lateinit var mUid: String

    private lateinit var mStopwatchHandler: StopwatchHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUid = Firebase.auth.currentUser?.uid.toString()
        setSupportActionBar(binding.topAppBar)

        isLocationSharing = readSharedPref()
        println("isLocationSharing: $isLocationSharing")

        mStopwatchHandler = StopwatchHandler(binding.sharingYourLocation)

        val userInfo: UserInfo? = mDatabase.getUserInfo()
        if (userInfo != null) {
            binding.loggedInAs.text =
                getString(R.string.logged_in_as_s, userInfo.email)
            binding.profileImage.loadSvg(userInfo.userImage)
        }

        if (isLocationSharing) {
            binding.shareLocation.text = getString(R.string.stop_sharing_location)
            binding.cardView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveToSharedPref(false)
    }

    private fun readSharedPref(): Boolean {
        val prefs: SharedPreferences = this.getSharedPreferences(
            "com.pixieium.austtravels", MODE_PRIVATE
        )
        return prefs.getBoolean("isLocationSharing", false)
    }


    private fun saveToSharedPref(res: Boolean) {
        val prefs = getSharedPreferences(
            "com.pixieium.austtravels", MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("isLocationSharing", res)
        editor.apply()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }


    private fun clearNotification() {
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun createNotification() {
        // creating an onclick intent
        val notificationIntent = Intent(this, HomeActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val intent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.ic_bus)
            .setContentTitle("Location sharing on")
            .setContentIntent(intent)
            .setContentText("You are currently sharing your location in the background")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
        //Before you can deliver the notification on Android 8.0 and higher, you must
        // register your app's notification channel with the system by passing an
        // instance of NotificationChannel to createNotificationChannel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID.toString(), name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun stopLocationSharing() {
        Toast.makeText(
            this@HomeActivity, "Location sharing turned off",
            Toast.LENGTH_SHORT
        ).show()
        mLocationManager.removeUpdates(myLocationListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Toast.makeText(this, "Signing out!", Toast.LENGTH_SHORT).show()
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startLocationSharing() {
        mLocationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        // check if location permission is enabled or not
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // show prominent disclosure dialog
            // after the user accepts the agreement,
            // build an alert dialog requesting for permission
            ProminentDisclosureDialog.newInstance()
                .show(supportFragmentManager, ProminentDisclosureDialog.TAG)
            return
        }

        // start sharing location
        createNotification()
        saveToSharedPref(true)

        binding.shareLocation.text = getString(R.string.stop_sharing_location)
        binding.cardView.visibility = View.VISIBLE
        isLocationSharing = true

        binding.busName.text = getString(R.string.bus_jamuna, mSelectedBusName)
        binding.busTime.text = getString(R.string.time_6_45_am, mSelectedBusTime)

        // check if GPS is enabled or not
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            mDatabase.updateLocation(mUid, mSelectedBusName, mSelectedBusTime, location)
        }

        myLocationListener = MyLocationListener()
        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5, 1f,
            myLocationListener
        )
        Toast.makeText(
            this@HomeActivity, "Started sharing location",
            Toast.LENGTH_SHORT
        ).show()
    }


    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            mDatabase.updateLocation(mUid, mSelectedBusName, mSelectedBusTime, location)
            /*  Toast.makeText(
                  this@HomeActivity, "Location changed!",
                  Toast.LENGTH_SHORT
              ).show()*/
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            Toast.makeText(
                this@HomeActivity, "$provider's status changed to $status!",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onProviderEnabled(provider: String) {
            Toast.makeText(
                this@HomeActivity, "Provider $provider enabled!",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onProviderDisabled(provider: String) {
            Toast.makeText(
                this@HomeActivity, "Provider $provider disabled!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val CHANNEL_ID = 745
        private var isLocationSharing = false
    }

    /*volunteer select dialog*/
    override fun onVolunteerApprovalClick() {
        lifecycleScope.launch {
            if (mDatabase.createVolunteer(mUid)) {
                Toast.makeText(
                    this@HomeActivity,
                    "You are now a volunteer! Start sharing your location!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@HomeActivity,
                    "Couldn't make you a volunteer at this moment. Try again later!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                startLocationSharing()
                startStopwatch()
                // createNotification();
            }
            REQUEST_LIVE_TRACK -> {
                val intent = Intent(this@HomeActivity, LiveTrackActivity::class.java)
                intent.putExtra("SELECTED_BUS_NAME", selectedBusName)
                intent.putExtra("SELECTED_BUS_TIME", selectedBusTime)
                startActivity(intent)
            }
        }

    }

    private fun startStopwatch() {
        mStopwatchHandler.sendEmptyMessage(MSG_START_TIMER)
    }

    private fun stopStopwatch() {
        mStopwatchHandler.sendEmptyMessage(MSG_STOP_TIMER)
    }


    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun buildAlertMessageNoPermission() {
        AlertDialog.Builder(this)
            .setTitle("Location Permission Needed")
            .setMessage("This app needs the Location permission, please accept to use location functionality")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                //Prompt the user to request permission
                requestLocationPermission()
            }
            .create()
            .show()
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            LiveTrackActivity.MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LiveTrackActivity.MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startLocationSharing()
                        // createNotification();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                        this,
                        "Permission denied! Please enable location permission to access this feature",
                        Toast.LENGTH_LONG
                    ).show()

                    // Check if we are in a state where the user has denied the permission and
                    // selected Don't ask again
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }

        }
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
        if (!isLocationSharing) {
            lifecycleScope.launch {
                val isVolunteer = mDatabase.isVolunteer(mUid)
                // if user is a volunteer
                if (isVolunteer) {
                    // open dialog to select bus
                    SelectBusDialog.newInstance(REQUEST_SHARE_LOCATION)
                        .show(supportFragmentManager, SelectBusDialog.TAG)
                } else {
                    // open dialog to prompt user to become a volunteer
                    PromptVolunteerDialog.newInstance()
                        .show(supportFragmentManager, PromptVolunteerDialog.TAG)
                }
            }

        } else {
            // stop sharing location
            binding.shareLocation.text = getString(R.string.share_location)
            stopLocationSharing()
            stopStopwatch()
            clearNotification()
            saveToSharedPref(false)
            binding.cardView.visibility = View.GONE
            isLocationSharing = false
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
        buildAlertMessageNoPermission()
    }
}


