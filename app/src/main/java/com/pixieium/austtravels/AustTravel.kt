package com.pixieium.austtravels

import android.app.Application
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import com.pixieium.austtravels.notification.NotificationApi
import com.pixieium.austtravels.utils.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// This is the entry point of the app
class AustTravel : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        // We can use this to send notice like notification
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.GENERAL_NOTIFICATION)
            .addOnSuccessListener {
                Toast.makeText(
                    getApplicationContext(),
                    "Success - ${Constant.GENERAL_NOTIFICATION}",
                    Toast.LENGTH_LONG
                ).show();
            }

        // For testing
//        FirebaseMessaging.getInstance().subscribeToTopic("test_at")
//            .addOnSuccessListener {
//                Toast.makeText(getApplicationContext(), "Success - test_at", Toast.LENGTH_LONG)
//                    .show();
//            }
    }

    companion object {

        fun notificationApi(): NotificationApi {
            return api().create(NotificationApi::class.java)
        }

        fun api(): Retrofit {
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