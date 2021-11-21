package com.pixieium.austtravels.models

data class BusInfo(
    val name: String,
    val time: String
) {
    constructor() : this(
        "",
        ""
    )
}
