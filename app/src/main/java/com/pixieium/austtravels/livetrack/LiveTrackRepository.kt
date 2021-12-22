package com.pixieium.austtravels.livetrack

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class LiveTrackRepository {
    suspend fun fetchBusRoute(busName: String, busTime: String): ArrayList<Route> {
        val routeList: ArrayList<Route> = ArrayList()
        try {
            val database = Firebase.database
            val snapshot = database.getReference("bus/$busName/$busTime/routes").get().await()
            if (snapshot.exists()) {
                for (snap: DataSnapshot in snapshot.children) {
                    snap.getValue<Route>()?.let { routeList.add(it) }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }

        return routeList
    }

    fun addWatcher(mSelectedBusName: String, mSelectedBusTime: String) {
        try {
            val database = Firebase.database
            database.getReference("bus/$mSelectedBusName/$mSelectedBusTime/viewers/${Firebase.auth.currentUser?.uid}")
                .setValue(true)

        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    fun removeWatcher(mSelectedBusName: String, mSelectedBusTime: String) {
        try {
            val database = Firebase.database
            database.getReference("bus/$mSelectedBusName/$mSelectedBusTime/viewers/${Firebase.auth.currentUser?.uid}")
                .setValue(null)

        } catch (e: Exception) {
            Timber.d(e)
        }
    }

}