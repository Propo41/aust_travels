package com.pixieium.austtravels.home

import android.location.Location
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class HomeRepository {

    companion object {
        const val TAG = "HomeRepository"
    }

    suspend fun fetchAllBusInfo(): ArrayList<BusInfo> {
        val list: ArrayList<BusInfo> = ArrayList()
        try {
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
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
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
            Timber.e(e, e.localizedMessage)
            return null
        }
        return null
    }


    fun updateLocation(
        uid: String,
        universityId: String,
        mSelectedBusName: String,
        mSelectedBusTime: String,
        location: Location
    ) {
        try {
            val payload = mapOf(
                "lat" to location.latitude.toString(),
                "long" to location.longitude.toString(),
                "lastUpdatedTime" to System.currentTimeMillis().toString(),
                "lastUpdatedVolunteer" to uid,
                "universityId" to universityId,
            ) as HashMap<String, String>
            val database = Firebase.database
            database.getReference("bus/$mSelectedBusName/$mSelectedBusTime/location")
                .setValue(payload)
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
    }

    suspend fun getUserInfo(): UserInfo {
        try {
            val database = Firebase.database
            val uid = Firebase.auth.currentUser?.uid
            val snap = database.getReference("users/$uid").get().await()

            if (snap.exists()) {
                val userInfo = snap.getValue<UserInfo>()

                val userSettings = setUserSettings(snap)
                if (userInfo != null) {
                    userInfo.settings = userSettings
                }

                println(userInfo)
                if (userInfo != null) {
                    return userInfo
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }

        return UserInfo(
            Firebase.auth.currentUser?.email,
            Firebase.auth.currentUser?.photoUrl.toString(),
            "N/A"
        )
    }

    private fun setUserSettings(snap: DataSnapshot): UserSettings {
        var isPingNotification = snap.child("settings/isPingNotification").value as Boolean?
        var isLocationNotification =
            snap.child("settings/isLocationNotification").value as Boolean?
        var bus = snap.child("settings/primaryBus").value as String?

        if (isPingNotification == null) {
            isPingNotification = true
        }
        if (isLocationNotification == null) {
            isLocationNotification = false
        }
        if (bus == null) {
            bus = "None"
        }
        return UserSettings(isPingNotification, isLocationNotification, bus)

    }

    fun updateContribution(totalTimeElapsed: Long) {
        try {
            // get the previous contribution first and then append
            val database = Firebase.database
            val uid = Firebase.auth.currentUser?.uid
            // keep this data fresh
            //Firebase.database.getReference("volunteers/$uid/totalContribution").keepSynced(true)
            database.getReference("volunteers/$uid/totalContribution").get().addOnSuccessListener {
                if (it.exists() && it != null) {
                    val prevTime: Long = it.value as Long
                    database.getReference("volunteers/$uid/totalContribution")
                        .setValue(totalTimeElapsed + prevTime)
                } else {
                    database.getReference("volunteers/$uid/totalContribution")
                        .setValue(totalTimeElapsed)
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
    }

}