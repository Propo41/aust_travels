package com.pixieium.austtravels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.home.HomeActivity

class MainActivity : AppCompatActivity() {
    val  mAuth =FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = mAuth.getCurrentUser()
        if (user != null) {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}