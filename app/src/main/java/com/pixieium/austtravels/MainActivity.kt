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
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // setUniversityInfo()
    }


    private fun setUniversityInfo() {
        val database = Firebase.database
        val semesters: ArrayList<String?> = object : java.util.ArrayList<String?>() {
            init {
                add("5.2")
                add("5.1")
                add("4.2")
                add("4.1")
                add("3.2")
                add("3.1")
                add("2.2")
                add("2.1")
                add("1.2")
                add("1.1")
            }
        }
        database.getReference("universityInfo/semesters").setValue(semesters)

        val departments: ArrayList<String?> = object : java.util.ArrayList<String?>() {
            init {
                add("CSE")
                add("EEE")
                add("CE")
                add("ME")
                add("IPE")
                add("TE")
                add("BBA")
                add("ARCH")
            }
        }

        database.getReference("universityInfo/departments").setValue(departments)
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