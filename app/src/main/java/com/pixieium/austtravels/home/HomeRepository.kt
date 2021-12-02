package com.pixieium.austtravels.home

import android.location.Location
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.UserInfo
import com.pixieium.austtravels.models.Volunteer
import kotlinx.coroutines.tasks.await

class HomeRepository {

    suspend fun fetchAllBusInfo(): ArrayList<BusInfo> {
        val list: ArrayList<BusInfo> = ArrayList()
        // Write a message to the database
        val database = Firebase.database
        val snapshot = database.getReference("availableBusInfo").get().await()
        if (snapshot.exists()) {
            // iterate over the timing
            for (snap: DataSnapshot in snapshot.children) {
                val busInfo = BusInfo()
                busInfo.name = snap.key.toString()

                val list2: ArrayList<BusTiming> = ArrayList()
                for (snap1: DataSnapshot in snap.children) {
                    snap1.getValue<BusTiming>()?.let { list2.add(it) }
                }

                busInfo.timing = list2
                list.add(busInfo)
            }
        }
        return list
    }

    suspend fun getVolunteerInfo(uid: String): Volunteer? {
        try {
            val database = Firebase.database
            val snapshot = database.getReference("volunteers/$uid").get().await()
            if (snapshot.exists() && snapshot != null) {
                return snapshot.getValue<Volunteer>()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }

    suspend fun getUserPrimaryBus(uid: String): String? {
        try {
            val database = Firebase.database
            val snapshot = database.getReference("users/$uid/primaryBus").get().await()
            if (snapshot.exists() && snapshot != null) {
                return snapshot.getValue<String>()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }

    fun updateLocation(
        uid: String,
        mSelectedBusName: String,
        mSelectedBusTime: String,
        location: Location
    ) {
        val payload = mapOf(
            "lat" to location.latitude.toString(),
            "long" to location.longitude.toString(),
            "lastUpdatedTime" to System.currentTimeMillis().toString(),
            "lastUpdatedVolunteer" to uid
        ) as HashMap<String, String>
        val database = Firebase.database
        database.getReference("bus/$mSelectedBusName/$mSelectedBusTime/location").setValue(payload)

    }

    fun getUserInfo(): UserInfo? {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val email = user.email
            val photoUrl = user.photoUrl
            return UserInfo(email, photoUrl.toString())
        }
        return null
    }

    fun updateContribution(totalTimeElapsed: Long) {
        // get the previous contribution first and then append
        val database = Firebase.database
        val uid = Firebase.auth.currentUser?.uid
        database.getReference("volunteers/$uid/totalContribution").get().addOnSuccessListener {
            if (it.exists() && it != null) {
                val prevTime: Long = it.value as Long
                database.getReference("volunteers/$uid/totalContribution")
                    .setValue(totalTimeElapsed + prevTime)
            }else{
                database.getReference("volunteers/$uid/totalContribution")
                    .setValue(totalTimeElapsed)
            }
        }

    }

}