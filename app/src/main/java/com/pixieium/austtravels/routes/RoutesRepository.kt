package com.pixieium.austtravels.routes

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RoutesRepository {
    fun fetchRouteList(busName: String, busTime: String) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("bus/$busName/$busTime/routes")
        myRef.setValue("Hello, World!")
    }


    suspend fun fetchAllBusInfo(): ArrayList<BusInfo> {
        // Write a message to the database
        val database = Firebase.database
        val snapshot = database.getReference("availableBusInfo").get().await()
        val list: ArrayList<BusInfo> = ArrayList()
        if (snapshot.exists()) {
            println("data found! downloading")
            try {
                for (snap: DataSnapshot in snapshot.children) {
                    snap.getValue<BusInfo>()?.let { list.add(it) }
                    println(snap.value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return list
    }


    fun updateAvailableBusInfo(list: ArrayList<BusInfo>) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("availableBusInfo")
        myRef.setValue(list)
    }

}