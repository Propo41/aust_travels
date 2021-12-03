package com.pixieium.austtravels.routes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.Exception

class RoutesRepository {
    suspend fun fetchRouteList(busName: String, busTime: String): ArrayList<Route> {
        val list: ArrayList<Route> = ArrayList()
        try {
            val database = Firebase.database
            val snapshot = database.getReference("bus/$busName/$busTime/routes").get().await()
            if (snapshot.exists()) {
                for (snap: DataSnapshot in snapshot.children) {
                    snap.getValue<Route>()?.let { list.add(it) }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }

        return list
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

}