package com.pixieium.austtravels

import android.app.Application
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.pixieium.austtravels.utils.Constant

// This is the entry point of the app
class AustTravel : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // We can use this to send notice like notification
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.GENERAL_NOTIFICATION)
            .addOnSuccessListener {
                Toast.makeText(
                    getApplicationContext(),
                    "Success - ${Constant.GENERAL_NOTIFICATION}",
                    Toast.LENGTH_LONG
                ).show();
            }

        // For testing
//        FirebaseMessaging.getInstance().subscribeToTopic("test_at")
//            .addOnSuccessListener {
//                Toast.makeText(getApplicationContext(), "Success - test_at", Toast.LENGTH_LONG)
//                    .show();
//            }
    }
}