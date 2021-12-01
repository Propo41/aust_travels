package com.pixieium.austtravels.volunteers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.UserInfo
import com.pixieium.austtravels.models.Volunteer
import kotlinx.coroutines.tasks.await

class VolunteerRepository {
    suspend fun fetchVolunteers(): ArrayList<Volunteer> {
        val volunteers: ArrayList<Volunteer> = ArrayList()

        try {
            val database = Firebase.database
            val snapshot =
                database.getReference("volunteers").orderByChild("totalContribution").get().await()
            val x = snapshot.key
            for (snap: DataSnapshot in snapshot.children) {
                val xy = snap.getValue<Volunteer>()
                if (xy != null) {
                    if (xy.isStatus) {
                        if (xy.totalContribution == null) {
                            xy.totalContribution = 0
                        }
                        val totalContributionFormatted =
                            convertContributionTime(xy.totalContribution / 1000)
                        val userInfo = getUserInfo(snap.key.toString())
                        if (userInfo != null) {
                            val volunteer = Volunteer(userInfo, totalContributionFormatted)
                            volunteers.add(volunteer)
                        }
                    }
                }
            }
            // reverse the list since the list comes in ascending order

            return volunteers.reversed() as ArrayList<Volunteer>

        } catch (e: Exception) {
            //e.printStackTrace()
        }

        return volunteers

    }

    /**
     * totalContribution in seconds
     * */
    private fun convertContributionTime(totalContribution: Long?): String {
        var time: Long = totalContribution ?: 0
        var str = time.toString() + "s"

        // if greater than 60s show in min
        if (time > 60) {
            time /= 60
            str = time.toString() + "m"
        }
        if (time > 99) {
            // fi greater than 99 mins, show in hrs
            time /= 60
            str = time.toString() + "hr"
        }
        if (time > 99) {
            // if greater than 99 hours, show in days
            time /= 24
            str = time.toString() + "d"
        }

        return str
    }


    private suspend fun getUserInfo(uid: String): UserInfo? {
        try {
            val database = Firebase.database
            val snapshot = database.getReference("users/$uid").get().await()
            if (snapshot.exists()) {
                return snapshot.getValue<UserInfo>()
            }
            return null
        } catch (e: Exception) {
            //e.printStackTrace()
        }

        return null

    }
}