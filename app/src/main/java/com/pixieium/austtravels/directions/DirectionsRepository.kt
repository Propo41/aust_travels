package com.pixieium.austtravels.directions

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await

class DirectionsRepository {
    suspend fun getBusRouteInfo(busName: String, busTime: String): ArrayList<Route> {
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