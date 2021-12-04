package com.pixieium.austtravels.utils.notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pixieium.austtravels.MainActivity
import com.pixieium.austtravels.models.Notification
import timber.log.Timber

/**
 * Created by snnafi on 26/04/18.
 */

class AustTravelFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Timber.d("Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: " + remoteMessage.data)
            val data = remoteMessage.data
            handleData(data)
        } else if (remoteMessage.notification != null) {
            Timber.d(
                "Message Notification Body: " + remoteMessage.notification!!
                    .body
            )

            handleNotification(remoteMessage.notification)
        } // Check if message contains a notification payload.
        super.onMessageReceived(remoteMessage)
    }

    private fun handleNotification(RemoteMsgNotification: RemoteMessage.Notification?) {
        val message = RemoteMsgNotification!!.body
        val title = RemoteMsgNotification.title
        val notificationModel = Notification()
        notificationModel.title = title!!
        notificationModel.message = message!!
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationModel, resultIntent)
       // notificationUtils.playNotificationSound()
    }

    private fun handleData(data: Map<String, String>) {
        val title = data[TITLE]
        val message = data[MESSAGE]
        val action = data[ACTION]
        val actionDestination = data[ACTION_DESTINATION]
        val notificationModel = Notification()
        notificationModel.title = title!!
        notificationModel.message = message!!
        notificationModel.action = action
        notificationModel.actionDestination = actionDestination

        Timber.d(notificationModel.toString())

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationModel, resultIntent)
       // notificationUtils.playNotificationSound()
    }

    companion object {
        private const val TAG = "aust_travel"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val ACTION = "action"
        private const val DATA = "data"
        private const val ACTION_DESTINATION = "action_destination"
    }
}