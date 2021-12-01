package com.pixieium.austtravels.notification

import com.squareup.okhttp.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query


interface NotificationApi {

    @GET("send-volunteer")
    suspend fun notifyVolunteers(
        @Query("bus") busName: String,
        @Query("title") title: String,
        @Query("message") message: String
    ): ResponseBody
}
