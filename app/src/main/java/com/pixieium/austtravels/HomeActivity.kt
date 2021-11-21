package com.pixieium.austtravels

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.dialog.SelectBusDialog

/* stop watch: https://stackoverflow.com/questions/3733867/stop-watch-logic*/
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mLocationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private val isVolunteer = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.shareLocation.setOnClickListener {
            if (!isLocationSharing) {
                // start sharing location
                binding.shareLocation.text = getString(R.string.stop_sharing_location)
                binding.cardView.visibility = View.VISIBLE
                startLocationSharing()
                // createNotification();
                isLocationSharing = true

                // if user is a volunteer
                if (isVolunteer) {
                    // open dialog to select bus
                    SelectBusDialog.newInstance("uid")
                        .show(supportFragmentManager, SelectBusDialog.TAG)
                } else {
                    // open dialog to prompt user to become a volunteer

                }
            } else {
                // stop sharing location
                binding.shareLocation.text = getString(R.string.share_location)
                stopLocationSharing()
                binding.cardView.visibility = View.GONE
                isLocationSharing = false
            }
        }
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

    private fun startLocationSharing() {
        mLocationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
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
                this@HomeActivity, "requires permission",
                Toast.LENGTH_SHORT
            ).show()
            return
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
}