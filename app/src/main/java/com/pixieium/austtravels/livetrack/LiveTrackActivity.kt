package com.pixieium.austtravels.livetrack

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.pixieium.austtravels.databinding.ActivityLiveTrackBinding

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location

import android.provider.Settings
import android.net.Uri
import android.view.MenuItem

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.home.HomeRepository
import com.pixieium.austtravels.models.BusInfo
import android.text.format.DateUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


// watch this for setting location permission at run time: https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
class LiveTrackActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLiveTrackBinding

    private val mDatabase: LiveTrackRepository = LiveTrackRepository()
    private lateinit var mSelectedBusName: String
    private lateinit var mSelectedBusTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLiveTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mSelectedBusName = intent.getStringExtra("SELECTED_BUS_NAME").toString()
        mSelectedBusTime = intent.getStringExtra("SELECTED_BUS_TIME").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.sec.text = getString(R.string.selected_bus, mSelectedBusName)
        binding.start.text = getString(R.string.starting_time, mSelectedBusTime)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // todo logout()
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    private fun fetchLocationInfo(busName: String, busTime: String) {
        val locListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val lat = dataSnapshot.child("lat").value.toString().toDouble()
                    val long = dataSnapshot.child("long").value.toString().toDouble()
                    val location = LatLng(lat, long)
                    moveToCurrentLocation(location)
                    val lastUpdated = dataSnapshot.child("lastUpdatedTime").value.toString()
                    binding.lastUpdated.text =
                        getString(R.string.last_updated, getRelativeTime(lastUpdated.toLong()))
                } else {
                    binding.lastUpdated.text =
                        getString(R.string.last_updated, "Never")
                    // center the map around AUST if no location available
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(23.763863, 90.406255)))
                    mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)

                    Toast.makeText(baseContext, "Oops. No location data found!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                databaseError.toException().printStackTrace()
            }
        }
        val database = Firebase.database
        database.getReference("bus/$busName/$busTime/location").addValueEventListener(locListener)
    }

    /**
     * @param oldDate a long representing time in ms. Note, long numbers have L at the end, ie 1592717400000L
     * @return a string representing the relative time, ie X min ago, June 10,2020, x days ago etc
     */
    fun getRelativeTime(oldDate: Long): String {
        val currentDate = Date()
        val currentDateLong: Long = currentDate.time
        val relativeTime = DateUtils
            .getRelativeTimeSpanString(oldDate, currentDateLong, 0L)
        return relativeTime.toString()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableCurrentLocation()
        setBusStopMarkers()
    }

    private fun setBusStopMarkers() {
        lifecycleScope.launch {
            val list: ArrayList<Route> = mDatabase.fetchBusRoute(mSelectedBusName, mSelectedBusTime)
            for (route: Route in list) {
                val loc: LatLng = LatLng(route.latitude.toDouble(), route.longitude.toDouble())
                val markerOptions = MarkerOptions().position(loc)
                    .title(route.place)
                    .snippet("Est. Time: ${route.estTime}")
                    .icon(
                        bitmapDescriptorFromVector(
                            baseContext,
                            R.drawable.ic_baseline_directions_24
                        )
                    )
                mMap.addMarker(markerOptions)
            }

        }
    }

    private fun enableCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fetchLocationInfo(mSelectedBusName, mSelectedBusTime)
        } else {
            // Show rationale and request permission.
            Toast.makeText(applicationContext, "You need to enable your GPS", Toast.LENGTH_SHORT)
                .show()
            buildAlertMessageNoGps()
        }

    }

    private fun buildAlertMessageNoGps() {
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
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mMap.isMyLocationEnabled = true
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

    private fun moveToCurrentLocation(currentLocation: LatLng) {
        val markerOptions = MarkerOptions().position(currentLocation)
            .title("Marker in Sydney")
            .snippet("snippet snippet snippet snippet snippet...")
            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_bus))
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }


}