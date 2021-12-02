package com.pixieium.austtravels.settings

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Payload
import com.pixieium.austtravels.models.UserSettings
import kotlinx.coroutines.tasks.await

class SettingsRepository {

    suspend fun createVolunteer(uid: String, busName: String, contact: String): Payload {
        return try {
            val database = Firebase.database
            // check if the user already made a request before
            // if not, then create a new entry in the database
            val snap = database.getReference("volunteers/$uid/status").get().await()
            if (!snap.exists()) {
                val childUpdates = hashMapOf<String, Any?>(
                    "/volunteers/${uid}/status" to false,
                    "/users/${uid}/settings/primaryBus" to busName,
                    "/volunteers/${uid}/contact" to contact,
                )
                database.reference.updateChildren(childUpdates).await()
                Payload("We've received your request and will shortly review it.", true)
            } else {
                if (snap.getValue<Boolean>() == true) {
                    Payload("You are already a volunteer! What else do you need?", false)
                } else {
                    Payload("Hey, hold your horses. We are reviewing your request!", false)
                }
            }
        } catch (e: Exception) {
            //e.printStackTrace()
            //Payload("Something went horribly wrong. Try again later!", false)
            Payload(e.localizedMessage, false)
        }
    }

    suspend fun deleteUser(password: String): Boolean {
        return try {
            val user = Firebase.auth.currentUser!!
            if (!reAuthenticateUser(password, user)) {
                return false
            }
            // delete info from database
            val database = Firebase.database.reference
            val childUpdates = hashMapOf<String, Any?>(
                "/users/${user.uid}" to null,
                "/volunteers/${user.uid}" to null
            )
            database.updateChildren(childUpdates)

            // delete auth user
            user.delete().await()
            true
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            //e.printStackTrace()
            false
        } catch (e: Exception) {
            //e.printStackTrace()
            false
        }
    }

    private suspend fun reAuthenticateUser(password: String, user: FirebaseUser): Boolean {
        try {
            val credential = EmailAuthProvider
                .getCredential(user.email!!, password)
            user.reauthenticate(credential).await()
            return true
        } catch (e: Exception) {
            //e.printStackTrace()
        }
        return false
    }

    suspend fun fetchAllBusInfo(): ArrayList<BusInfo> {
        val list: ArrayList<BusInfo> = ArrayList()
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
        return list
    }

    suspend fun getUserSettings(mUid: String): UserSettings? {
        return try {
            val database = Firebase.database
            val snapshot = database.getReference("users/$mUid/settings").get().await();
            if (snapshot.exists()) {
                snapshot.getValue<UserSettings>()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updatePrimaryBus(mUid: String, busName: String) {
        try {
            val database = Firebase.database
            database.getReference("users/$mUid/settings/primaryBus").setValue(busName)
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    fun updatePingNotificationSettings(mUid: String, checked: Boolean) {
        try {
            val database = Firebase.database
            database.getReference("users/$mUid/settings/isPingNotification").setValue(checked)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateLocationNotificationSettings(mUid: String, checked: Boolean) {
        try {
            val database = Firebase.database
            database.getReference("users/$mUid/settings/isLocationNotification").setValue(checked)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}