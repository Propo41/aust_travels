package com.pixieium.austtravels.models

data class BusInfo(
    var name: String,
    var timing: ArrayList<BusTiming>,
) {
    constructor() : this(
        "",
        ArrayList()
    )
}
