package com.pixieium.austtravels.home

import android.location.Location
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeRepository {

    suspend fun fetchAllBusInfo(): ArrayList<BusInfo> {
        // Write a message to the database
        val database = Firebase.database
        val snapshot = database.getReference("availableBusInfo").get().await()
        val list: ArrayList<BusInfo> = ArrayList()
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

    suspend fun createVolunteer(uid: String): Boolean {
        val database = Firebase.database
        return try {
            database.getReference("volunteers/$uid").setValue(true).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun isVolunteer(uid: String): Boolean {
        val database = Firebase.database
        return try {
            val snapshot = database.getReference("volunteers/$uid").get().await()
            snapshot.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
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

}
