package com.pixieium.austtravels.auth

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.models.UserInfo
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthRepository {
    suspend fun getSemesterInfo(): ArrayList<String> {
        val database = Firebase.database
        val snapshot = database.getReference("universityInfo/semesters").get().await()
        val semesterList: ArrayList<String> = ArrayList()
        if (snapshot.exists()) {
            for (snap: DataSnapshot in snapshot.children) {
                semesterList.add(snap.value.toString())
            }
        }
        return semesterList
    }

    suspend fun getDeptInfo(): ArrayList<String> {
        val database = Firebase.database
        val snapshot = database.getReference("universityInfo/departments").get().await()
        val departmentList: ArrayList<String> = ArrayList()
        if (snapshot.exists()) {
            for (snap: DataSnapshot in snapshot.children) {
                departmentList.add(snap.value.toString())
            }
        }
        return departmentList
    }

    suspend fun createNewUser(userInfo: UserInfo, uid: String): Boolean {
        val database = Firebase.database
        return try {
            val snapshot = database.getReference("users/$uid").setValue(userInfo).await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }


    }
}