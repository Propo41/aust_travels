package com.pixieium.austtravels.models

data class BusTiming(
    val startTime: String,
    val departureTime: String
) {
    constructor() : this(
        "",
        ""
    )
}
