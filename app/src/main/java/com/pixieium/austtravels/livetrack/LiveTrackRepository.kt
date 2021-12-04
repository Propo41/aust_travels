package com.pixieium.austtravels.livetrack

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class LiveTrackRepository {
    // todo: get the bus departure time
    suspend fun getBusInfo(busName: String, busTime: String) {
        val database = Firebase.database
        val snapshot = database.getReference("bus/$busName/$busTime").get().await()
        snapshot.exists()
    }

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




}