package com.pixieium.austtravels.settings

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.Payload
import kotlinx.coroutines.tasks.await

class SettingsRepository {
    suspend fun createVolunteer(uid: String): Payload {
        return try {
            val database = Firebase.database
            // check if the user already made a request before
            // if not, then create a new entry in the database
            val snap = database.getReference("volunteers/$uid/status").get().await()
            if (!snap.exists()) {
                database.getReference("volunteers/$uid/status").setValue(false).await()
                Payload("We've received your request and will shortly review it.", true)
            } else {
                Payload("Hey, hold your horses. We are reviewing your request!", false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Payload("Something went horribly wrong. Try again later!", false)
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
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
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
            e.printStackTrace()
        }
        return false
    }

}