package com.pixieium.austtravels.routes

import android.hardware.lights.LightState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Route
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RoutesRepository {
    suspend fun fetchRouteList(busName: String, busTime: String): ArrayList<Route> {
        val database = Firebase.database
        val snapshot = database.getReference("bus/$busName/$busTime/routes").get().await()
        val list: ArrayList<Route> = ArrayList()
        if (snapshot.exists()) {
            for (snap: DataSnapshot in snapshot.children) {
                snap.getValue<Route>()?.let { list.add(it) }
                println(snap.value)
            }
        }
        return list
    }

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
        println(list)

        return list
    }


    fun updateAvailableBusInfo(list: HashMap<String, ArrayList<BusTiming>>) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("availableBusInfo")
        myRef.setValue(list)
    }

    fun updateBusRoute(busName: String, busTime: String, list: ArrayList<Route>) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("bus/$busName/$busTime/routes")
        myRef.setValue(list)
    }

}