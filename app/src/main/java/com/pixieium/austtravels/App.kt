package com.pixieium.austtravels

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.pixieium.austtravels.utils.Constant
import com.pixieium.austtravels.utils.notification.NotificationApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit


// This is the entry point of the app
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // setting disk persistence on
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseApp.initializeApp(this)

        // only use timber in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // We can use this to send notice like notification
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.GENERAL_NOTIFICATION)
            .addOnSuccessListener {
            }

    }

    companion object {
        fun notificationApi(): NotificationApi {
            return api().create(NotificationApi::class.java)
        }

        private fun api(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client)
                .build()
        }
    }
}