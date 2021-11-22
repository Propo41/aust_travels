package com.pixieium.austtravels

import android.content.Intent
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.home.HomeActivity
import kotlinx.coroutines.tasks.await
import android.location.Geocoder
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance();
    private val mDatabase: MainRepository = MainRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*
        // the following are dummy queries
         mDatabase.setUniversityInfo()
         mDatabase.pushBusRouteInfo("Jamuna", "6:30AM")
         mDatabase.pushAllBusInfo()
         */
    }

    override fun onStart() {
        super.onStart()

        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }
}