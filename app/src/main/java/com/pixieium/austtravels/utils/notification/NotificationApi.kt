package com.pixieium.austtravels.utils.notification

import retrofit2.http.GET
import retrofit2.http.Query


interface NotificationApi {

    @GET("send-volunteer")
    suspend fun notifyVolunteers(
        @Query("bus") busName: String,
        @Query("title") title: String,
        @Query("message") message: String
    )

    @GET("send-users")
    suspend fun notifyUsers(
        @Query("bus") busName: String,
        @Query("title") title: String,
        @Query("message") message: String
    )
}
