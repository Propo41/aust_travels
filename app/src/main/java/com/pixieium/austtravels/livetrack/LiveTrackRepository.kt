package com.pixieium.austtravels.livetrack

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await

class LiveTrackRepository {
    // todo: get the bus departure time
    suspend fun getBusInfo(busName: String, busTime: String) {
        val database = Firebase.database
        val snapshot = database.getReference("bus/$busName/$busTime").get().await()
        snapshot.exists()
    }

    suspend fun fetchBusRoute(busName: String, busTime: String): ArrayList<Route> {
        val database = Firebase.database
        val snapshot = database.getReference("bus/$busName/$busTime/routes").get().await()
        val routeList: ArrayList<Route> = ArrayList()
        if (snapshot.exists()) {
            for (snap: DataSnapshot in snapshot.children) {
                snap.getValue<Route>()?.let { routeList.add(it) }
            }
        }
        return routeList


    }


}