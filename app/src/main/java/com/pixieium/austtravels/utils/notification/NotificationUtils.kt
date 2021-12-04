package com.pixieium.austtravels.utils.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pixieium.austtravels.MainActivity
import com.pixieium.austtravels.R
import timber.log.Timber
import java.util.*


/**
 * Created by snnafi on 26/04/18.
 */


class NotificationUtils(private val mContext: Context) {
    var activityMap: MutableMap<String, Class<*>> = HashMap()
    var time = System.currentTimeMillis()

    /**
     * Displays notification based on parameters
     *
     * @param notificationVO
     * @param resultIntent
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    fun displayNotification(
        notificationl: com.pixieium.austtravels.models.Notification,
        resultIntent: Intent
    ) {
        var resultIntent = resultIntent
        run {
            val message: String = notificationl.message
            val title: String = notificationl.title
            val action: String? = notificationl.action
            val destination: String? = notificationl.actionDestination
            val icon: Int = R.mipmap.ic_launcher
            val resultPendingIntent: PendingIntent
            when {
                URL == action -> {
                    val notificationIntent: Intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(destination))
                    resultPendingIntent =
                        PendingIntent.getActivity(mContext, 0, notificationIntent, 0)
                }
                ACTIVITY == action -> {
                    resultIntent = Intent(mContext, activityMap.get(destination))
                    resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                }
                else -> {
                    resultIntent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    resultPendingIntent = PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                    )
                }
            }
            val mBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(
                    mContext, CHANNEL_ID
                )
            val notification: Notification

            //When Inbox Style is applied, user can expand the notification
            val inboxStyle: NotificationCompat.InboxStyle =
                NotificationCompat.InboxStyle()
            inboxStyle.addLine(message)

            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_bus)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setStyle(inboxStyle)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, icon))
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setWhen(time)
                .setShowWhen(true)
                .build()

            val notificationManager: NotificationManager =
                mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //All notifications should go through NotificationChannel on Android 26 & above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel: NotificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }


    /**
     * Playing notification sound
     */
    fun playNotificationSound() {
        try {
            val alarmSound = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + mContext.packageName + "/raw/notification"
            )
            val r = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 200
        private const val CHANNEL_ID = "aust_notify_id"
        private const val CHANNEL_NAME = "aust_notify"
        private const val URL = "url"
        private const val ACTIVITY = "activity"
    }

    init {

//        Populate activity map. We can present a specefic activity by tapping the notification
        activityMap["default"] = MainActivity::class.java

    }
}