package com.pixieium.austtravels

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.home.HomeActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance();
    private val mDatabase: MainRepository = MainRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main)

        // the following are dummy queries
       //  mDatabase.setUniversityInfo()
      //   mDatabase.pushBusRouteInfo("Jamuna", "6:30AM")
         //mDatabase.pushAllBusInfo()

    }

    override fun onStart() {
        super.onStart()
        //mDatabase.pushBusRouteInfo1("Jamuna", "8:30AM")
       // mDatabase.pushBusRouteInfo2("Jamuna", "6:30AM")

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