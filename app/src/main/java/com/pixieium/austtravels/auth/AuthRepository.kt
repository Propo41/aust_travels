package com.pixieium.austtravels.auth

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.UserInfo
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class AuthRepository {
    suspend fun getSemesterInfo(): ArrayList<String> {
        val semesterList: ArrayList<String> = ArrayList()
        try {
            val database = Firebase.database
            val snapshot = database.getReference("universityInfo/semesters").get().await()
            if (snapshot.exists()) {
                for (snap: DataSnapshot in snapshot.children) {
                    semesterList.add(snap.value.toString())
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
        return semesterList
    }

    suspend fun getDeptInfo(): ArrayList<String> {
        val departmentList: ArrayList<String> = ArrayList()
        try {
            val database = Firebase.database
            val snapshot = database.getReference("universityInfo/departments").get().await()
            if (snapshot.exists()) {
                for (snap: DataSnapshot in snapshot.children) {
                    departmentList.add(snap.value.toString())
                }
            }
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }

        return departmentList
    }

    suspend fun saveNewUserInfo(userInfo: UserInfo, uid: String, user: FirebaseUser): Boolean {
        val database = Firebase.database
        val profileUpdates = userProfileChangeRequest {
            displayName = userInfo.name
            photoUri = Uri.parse(userInfo.userImage)
        }
        return try {
            user.updateProfile(profileUpdates).await()
            database.getReference("users/$uid").setValue(userInfo).await()
            true
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
            false
        }
    }
}