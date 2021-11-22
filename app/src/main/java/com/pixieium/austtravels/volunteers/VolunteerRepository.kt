package com.pixieium.austtravels.volunteers

import android.provider.ContactsContract
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.models.UserInfo
import kotlinx.coroutines.tasks.await

class VolunteerRepository {

    suspend fun fetchVolunteers(): ArrayList<UserInfo> {
        val database = Firebase.database
        val snapshot = database.getReference("volunteers").get().await()
        val volunteers: ArrayList<UserInfo> = ArrayList()
        for (snap: DataSnapshot in snapshot.children) {
            if (snap.getValue<Boolean>() == true) {
                getUserInfo(snap.key.toString())?.let { volunteers.add(it) }
            }
        }
        return volunteers
    }


    private suspend fun getUserInfo(uid: String): UserInfo? {
        val database = Firebase.database
        val snapshot = database.getReference("users/$uid").get().await()
        if (snapshot.exists()) {
            return snapshot.getValue<UserInfo>()
        }
        return null
    }
}