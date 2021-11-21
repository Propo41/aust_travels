package com.pixieium.austtravels.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.livetrack.LiveTrackActivity
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.directions.DirectionsActivity
import com.pixieium.austtravels.routes.RoutesActivity

/* stop watch: https://stackoverflow.com/questions/3733867/stop-watch-logic*/

class HomeActivity : AppCompatActivity(), PromptVolunteerDialog.FragmentListener,
    SelectBusDialog.FragmentListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mLocationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private var isVolunteer = false

    private val REQUEST_LIVE_TRACK = 0
    private val REQUEST_DIRECTIONS = 1
    private val REQUEST_SHARE_LOCATION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    /*todo: incomplete*/
    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID.toString())
            .setSmallIcon(R.drawable.ic_bus)
            .setContentTitle("Location sharing on")
            .setContentText("you are sharing your location")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(69, builder.build())
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
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

            return true
        }
        return false
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
            Toast.makeText(
                this@HomeActivity, "Requires location permission",
                Toast.LENGTH_SHORT
            ).show()
            buildAlertMessageNoPermission()
            return
        }

        // check if GPS is enabled or not
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
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
            Toast.makeText(
                this@HomeActivity, "Location changed!",
                Toast.LENGTH_SHORT
            ).show()
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
        // todo make the user a volunteer in firebase
        Toast.makeText(
            this@HomeActivity,
            "You are now a volunteer! Press the button again",
            Toast.LENGTH_SHORT
        ).show()
        isVolunteer = true
    }

    override fun onBusSelectClick(
        selectedBusName: String,
        selectedBusTime: String,
        requestCode: Int
    ) {
        when (requestCode) {
            REQUEST_SHARE_LOCATION -> {
                // start sharing location
                binding.shareLocation.text = getString(R.string.stop_sharing_location)
                binding.cardView.visibility = View.VISIBLE
                isLocationSharing = true

                startLocationSharing()
                // createNotification();
            }
            REQUEST_DIRECTIONS -> {
                val intent = Intent(this@HomeActivity, DirectionsActivity::class.java)
                intent.putExtra("SELECTED_BUS_NAME", selectedBusName)
                intent.putExtra("SELECTED_BUS_TIME", selectedBusTime)
                startActivity(intent)
            }
            REQUEST_LIVE_TRACK -> {
                val intent = Intent(this@HomeActivity, LiveTrackActivity::class.java)
                intent.putExtra("SELECTED_BUS_NAME", selectedBusName)
                intent.putExtra("SELECTED_BUS_TIME", selectedBusTime)
                startActivity(intent)
            }
        }

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

    private fun enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationSharing()
            // createNotification();
        } else {
            // Show rationale and request permission.
            Toast.makeText(applicationContext, "You need to enable your GPS", Toast.LENGTH_SHORT)
                .show()
            buildAlertMessageNoPermission()
        }

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
            // if user is a volunteer
            if (isVolunteer) {
                // open dialog to select bus
                SelectBusDialog.newInstance(REQUEST_SHARE_LOCATION)
                    .show(supportFragmentManager, SelectBusDialog.TAG)
            } else {
                // open dialog to prompt user to become a volunteer
                PromptVolunteerDialog.newInstance("uid")
                    .show(supportFragmentManager, PromptVolunteerDialog.TAG)
            }
        } else {
            // stop sharing location
            binding.shareLocation.text = getString(R.string.share_location)
            stopLocationSharing()
            binding.cardView.visibility = View.GONE
            isLocationSharing = false
        }
    }

    fun onViewVolunteersClick(view: View) {}
}