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

    suspend fun getUserPrimaryBus(uid: String): String? {
        try {
            val database = Firebase.database
            val snapshot = database.getReference("users/$uid/primaryBus").get().await()
            if (snapshot.exists() && snapshot != null) {
                return snapshot.getValue<String>()
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
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
        try {
            val payload = mapOf(
                "lat" to location.latitude.toString(),
                "long" to location.longitude.toString(),
                "lastUpdatedTime" to System.currentTimeMillis().toString(),
                "lastUpdatedVolunteer" to uid
            ) as HashMap<String, String>
            val database = Firebase.database
            database.getReference("bus/$mSelectedBusName/$mSelectedBusTime/location")
                .setValue(payload)
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
    }

    suspend fun getUserInfo(): UserInfo? {
        try {
            val database = Firebase.database
            val uid = Firebase.auth.currentUser?.uid
            val snap = database.getReference("users/$uid/name").get().await()

            var name: String? = ""
            if (snap.exists()) {
                name = snap.value as String?
                if (name == null) {
                    name = "Somebody"
                }
            } else {
                name = "Somebody"
            }

            val user = Firebase.auth.currentUser
            user?.let {
                // Name, email address, and profile photo Url
                val email = user.email
                val photoUrl = user.photoUrl
                return UserInfo(email, photoUrl.toString(), name)
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }

        return null
    }

    fun updateContribution(totalTimeElapsed: Long) {
        try {
            // get the previous contribution first and then append
            val database = Firebase.database
            val uid = Firebase.auth.currentUser?.uid
            // keep this data fresh
            Firebase.database.getReference("volunteers/$uid/totalContribution").keepSynced(true)
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