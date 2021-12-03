package com.pixieium.austtravels.livetrack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.App
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityLiveTrackBinding
import com.pixieium.austtravels.home.dialog.ProminentDisclosureDialog
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.settings.SettingsActivity
import com.pixieium.austtravels.utils.Constant.PACKAGE_NAME
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


// watch this for setting location permission at run time: https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime
class LiveTrackActivity : AppCompatActivity(), OnMapReadyCallback,
    ProminentDisclosureDialog.FragmentListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLiveTrackBinding

    private val mDatabase: LiveTrackRepository = LiveTrackRepository()
    private lateinit var mSelectedBusName: String
    private lateinit var mSelectedBusTime: String

    private lateinit var mBusLocation: LatLng
    private var mBusMarker: Marker? = null
    private var isFirstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLiveTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.lastUpdated.visibility = View.GONE

        mSelectedBusName = intent.getStringExtra("SELECTED_BUS_NAME").toString()
        mSelectedBusTime = intent.getStringExtra("SELECTED_BUS_TIME").toString()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // bus name
        binding.sec.text = getString(R.string.selected_bus, mSelectedBusName)
        // bus start time
        binding.start.text = getString(R.string.starting_time, mSelectedBusTime)
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
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    private fun fetchLocationInfo(busName: String, busTime: String) {
        binding.lastUpdated.visibility = View.VISIBLE
        val locListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    binding.floatingActionButton.visibility = View.VISIBLE

                    val lat = dataSnapshot.child("lat").value.toString().toDouble()
                    val long = dataSnapshot.child("long").value.toString().toDouble()
                    mBusLocation = LatLng(lat, long)
                    moveToCurrentLocation(mBusLocation)
                    val lastUpdated = dataSnapshot.child("lastUpdatedTime").value.toString()
                    binding.lastUpdated.text =
                        getString(R.string.last_updated, getRelativeTime(lastUpdated.toLong()))
                } else {
                    binding.floatingActionButton.visibility = View.GONE

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
                Timber.e(databaseError.toException(), databaseError.toException().localizedMessage)
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

    private fun getLastPingTime(busName: String): Long {
        val prefs: SharedPreferences = this.getSharedPreferences(
            "com.pixieium.austtravels", MODE_PRIVATE
        )
        return prefs.getLong("PING_$busName", 0)
    }


    private fun saveLastPingTime(time: Long, busName: String) {
        val prefs = getSharedPreferences(
            PACKAGE_NAME, MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putLong("PING_$busName", time)
        editor.apply()
    }

    fun onPingClick(view: View) {
        val delayMinutes: Long = 5
        val diff = System.currentTimeMillis() - getLastPingTime(mSelectedBusName)
        if (diff >= TimeUnit.MINUTES.toMillis(delayMinutes)) {
            // if 5 minutes have passed since the last ping,
            // send push notifications to all volunteers of this bus
            Toast.makeText(
                this@LiveTrackActivity,
                "Hold on! Letting the volunteers know.",
                Toast.LENGTH_LONG
            ).show()
            saveLastPingTime(System.currentTimeMillis(), mSelectedBusName)

            lifecycleScope.launch {
                try {
                    App.notificationApi().notifyVolunteers(
                        mSelectedBusName,
                        "Somebody needs help",
                        "A fellow traveler wants to know where your bus, $mSelectedBusName of $mSelectedBusTime is located. You might wanna help them out!"
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        this@LiveTrackActivity,
                        e.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        } else {

            val timeSpent =
                TimeUnit.MINUTES.toMillis(
                    delayMinutes
                ) - diff

            val remaining =
                TimeUnit.MILLISECONDS.toSeconds(delayMinutes) - TimeUnit.MILLISECONDS.toSeconds(
                    timeSpent
                )

            Toast.makeText(
                this@LiveTrackActivity,
                "Hey! Don't be hasty. Wait ${abs(remaining)} more seconds before sending your next ping",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun buildMapUrl(origin: String, destination: String): String {
        return "https://www.google.com/maps/dir/?api=1&" +
                "origin=$origin&" +
                "destination=$destination&" +
                "travelmode=driving"
    }

    private fun setBusStopMarkers() {
        lifecycleScope.launch {
            val list: ArrayList<Route> = mDatabase.fetchBusRoute(mSelectedBusName, mSelectedBusTime)
            for (route: Route in list) {
                val loc = LatLng(route.latitude.toDouble(), route.longitude.toDouble())

                val markerOptions = MarkerOptions().position(loc)
                    .title(route.mapPlaceName)
                    .snippet("Est. Time: ${route.estTime}")
                    .icon(
                        bitmapDescriptorFromVector(
                            baseContext,
                            R.drawable.ic_baseline_directions_24
                        )
                    )

                customizeMapMarkers()
                mMap.addMarker(markerOptions)
            }
        }
    }


    private fun customizeMapMarkers() {
        // make the dialog boxes clickable
        mMap.setOnInfoWindowClickListener { arg0 ->
            // build a map URL from the current bus location to selected bus stop
            val busCurrentLocationName: String? = getPlaceNameFromCords(mBusLocation)
            if (busCurrentLocationName != null) {
                // building the map url
                // follow https://developers.google.com/maps/documentation/urls/get-started#directions-action
                val mapUrl = buildMapUrl(
                    encodePlaceName(busCurrentLocationName),
                    encodePlaceName(arg0.title)
                )

                Toast.makeText(
                    baseContext,
                    "Opening directions in map from : $busCurrentLocationName to ${arg0.title}",
                    Toast.LENGTH_SHORT
                ).show()

                createMapsIntent(mapUrl)

            } else {
                Toast.makeText(
                    baseContext,
                    "Couldn't find a suitable name for the bus location. Please try reloading the page",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // customize the dialog contents
        mMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val info = LinearLayout(baseContext)
                info.orientation = LinearLayout.VERTICAL

                val title = TextView(baseContext)
                title.setTextColor(Color.BLACK)
                title.gravity = Gravity.CENTER
                title.setTypeface(null, Typeface.BOLD)
                title.text = marker.title
                title.setTextAppearance(R.style.TextAppearance_AppCompat_Caption)

                val snippet = TextView(baseContext)
                snippet.setTextColor(Color.GRAY)
                snippet.setTextAppearance(R.style.TextAppearance_AppCompat_Body1)
                snippet.text = marker.snippet

                val prompt = TextView(baseContext)
                prompt.setTextColor(Color.BLUE)
                prompt.setTextAppearance(R.style.TextAppearance_AppCompat_Body2)
                prompt.text = getString(R.string.marker_prompt)

                info.addView(title)
                info.addView(snippet)
                info.addView(prompt)

                return info
            }
        })

    }

    /**
     * creating an intent to open the URI in google maps
     */
    private fun createMapsIntent(mapUrl: String) {
        val gmmIntentUri = Uri.parse(mapUrl)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        // if the client device has ability to handle the intent, then do so
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(
                baseContext,
                "Couldn't open the map. Do you have the latest version of Google Maps installed?",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Converts empty spaces to + to be usable by MAP URLs
     * Converts ',' to %2C
     */
    private fun encodePlaceName(str: String?): String {
        var newStr = ""
        if (str != null) {
            for (ch: Char in str) {
                when (ch) {
                    '/' -> {
                        newStr += "%2F"
                    }
                    ',' -> {
                        newStr += "%2C"
                    }
                    ' ' -> {
                        newStr += "+"
                    }
                    else -> {
                        newStr += ch
                    }
                }
            }
        }
        return newStr
    }


    /**
     * Refer to
     * https://stackoverflow.com/questions/2296377/how-to-get-city-name-from-latitude-and-longitude-coordinates-in-google-maps
     */
    private fun getPlaceNameFromCords(location: LatLng): String? {
        val gcd = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(location.latitude, location.longitude, 1)
        return if (addresses.isNotEmpty()) {
            addresses[0].getAddressLine(0)
        } else {
            null
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
            // show prominent disclosure dialog
            // after the user accepts the agreement,
            // build an alert dialog requesting for permission
            ProminentDisclosureDialog.newInstance()
                .show(supportFragmentManager, ProminentDisclosureDialog.TAG)
            Toast.makeText(applicationContext, "You need to enable your GPS", Toast.LENGTH_SHORT)
                .show()
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
                        fetchLocationInfo(mSelectedBusName, mSelectedBusTime)
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
            .title(mSelectedBusName)
            .snippet("Start time: $mSelectedBusTime")
            .icon(bitmapDescriptorFromVector(this, R.drawable.ic_bus_marker))

        // removing the previous marker
        if (mBusMarker != null) {
            mBusMarker!!.remove()
        }
        mBusMarker = mMap.addMarker(markerOptions)

        if (isFirstTime) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
            isFirstTime = false
        }
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

    override fun onDisclosureAcceptClick() {
        // after user accepts the agreement, request for permission to enable GPS
        buildAlertMessageNoGps()
    }

    fun onRepositionBusClick(view: View) {
        try {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mBusLocation))
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15F), 2000, null)
            isFirstTime = false
        } catch (e: IllegalStateException) {
            Timber.e(e)
        }


    }
}