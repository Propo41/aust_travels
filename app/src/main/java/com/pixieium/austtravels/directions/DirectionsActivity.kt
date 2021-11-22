package com.pixieium.austtravels.directions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityDirectionsBinding
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.launch

class DirectionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDirectionsBinding
    private val mDatabase: DirectionsRepository = DirectionsRepository()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val selectedBusName = intent.getStringExtra("SELECTED_BUS_NAME")
        val selectedBusTime = intent.getStringExtra("SELECTED_BUS_TIME")

        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        lifecycleScope.launch {
            if (selectedBusName != null && selectedBusTime != null) {
                val routeList: ArrayList<Route> =
                    mDatabase.getBusRouteInfo(selectedBusName, selectedBusTime)

                val mapUrl: String? = buildMapUrl(routeList)
                if (mapUrl != null) {
                    //println(mapUrl)
                    binding.webView.loadUrl(mapUrl)
                }
            }
        }

        setSupportActionBar(binding.toolbar)
        Toast.makeText(this, "$selectedBusName $selectedBusTime", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun buildMapUrl(routeList: ArrayList<Route>): String? {
        if (routeList.isNotEmpty()) {
            var wayPoints = ""
            var placeIds = ""
            for ((i, item: Route) in routeList.withIndex()) {
                wayPoints += encodeEmptySpaces(item.mapPlaceName)
                placeIds += item.mapPlaceId
                if (i != routeList.size - 1) {
                    wayPoints += "%7C"
                    placeIds += "%7C"
                }
            }

            val travelMode = "driving"
            return "https://www.google.com/maps/dir/?api=1&" +
                    "origin=${encodeEmptySpaces(routeList[0].mapPlaceName)}&" +
                    "destination=${encodeEmptySpaces(routeList[routeList.size - 1].mapPlaceName)}&" +
                    "travelmode=$travelMode&" +
                    "waypoints=$wayPoints&" +
                    "waypoint_place_ids=$placeIds"
        }
        return null
    }

    private fun encodeEmptySpaces(str: String): String {
        var newStr: String = ""
        for (ch: Char in str) {
            if (ch == ' ') {
                newStr += "%2C"
            } else {
                newStr += ch
            }
        }
        return newStr

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            Toast.makeText(this, "Signing out!", Toast.LENGTH_SHORT).show()
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }
}