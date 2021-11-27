package com.pixieium.austtravels

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Route
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class MainRepository {
    fun setUniversityInfo() {
        val database = Firebase.database
        val semesters: ArrayList<String?> = object : java.util.ArrayList<String?>() {
            init {
                add("5.2")
                add("5.1")
                add("4.2")
                add("4.1")
                add("3.2")
                add("3.1")
                add("2.2")
                add("2.1")
                add("1.2")
                add("1.1")
            }
        }
        database.getReference("universityInfo/semesters").setValue(semesters)

        val departments: ArrayList<String?> = object : java.util.ArrayList<String?>() {
            init {
                add("CSE")
                add("EEE")
                add("CE")
                add("ME")
                add("IPE")
                add("TE")
                add("BBA")
                add("ARCH")
            }
        }

        database.getReference("universityInfo/departments").setValue(departments)
    }

    fun pushBusRouteInfo(busName: String, busTime: String) {
        val list: ArrayList<Route> = ArrayList()

        list.add(
            Route(
                "Mohammadpur",
                "6:45AM",
                "Mohammadpur Bus Stand",
                "ChIJl893rOq_VTcR33N-LYm16FY",
                "23.757056",
                "90.361334"
            )
        )
        list.add(
            Route(
                "Shankar",
                "6:50AM",
                "Chhayanaut Shangskriti-Bhavan",
                "ChIJnSqg11G_VTcR-h5xzrJhIKU",
                "23.750767",
                "90.368380"
            )
        )
        list.add(
            Route(
                "Dhanmondi 15",
                "6:55AM",
                "15 No Bus Stand",
                "ChIJf1nIdku_VTcRPzHP7aTpLzA",
                "23.744144",
                "90.372813"
            )
        )
        list.add(
            Route(
                "Jigatola", "7:00AM", "Zigatola Bus Stand", "ChIJNU2SmMq4VTcR96mKYWZRdYA",
                "23.739193", "90.375611"
            )
        )
        list.add(
            Route(
                "City College",
                "7:05AM",
                "City College Bus Stop",
                "ChIJLUUO3re4VTcRh7hTzBQ5x-4",
                "23.739430", "90.383068"
            )
        )
        list.add(
            Route(
                "Dhanmondi 6",
                "7:10AM",
                "Road No. 6",
                "EiBSZCBOby4gNiwgRGhha2EgMTIwNSwgQmFuZ2xhZGVzaCIuKiwKFAoSCevE0-q2uFU3EWAQXREWw81NEhQKEgmBawKHsLhVNxHCBFndu2Oljw",
                "23.743421", "90.382232"
            )
        )
        list.add(
            Route(
                "Kalabagan",
                "7:15AM",
                "Kalabagan Bus Stoppage",
                "ChIJy6QbK3e5VTcR5LJ0opz09Ik",
                "23.747743", "90.380171"
            )
        )
        list.add(
            Route(
                "Rasel Square",
                "7:15AM",
                "Kalabagan Bus Stoppage",
                "ChIJy6QbK3e5VTcR5LJ0opz09Ik",
                "23.751643", "90.378685"
            )
        )
        list.add(
            Route(
                "Panthapath",
                "7:20AM",
                "Basnhundhara City North Bus Stop",
                "ChIJ58PGVLu4VTcRsUV-M6pcObs",
                "23.751065", "90.387459"
            )
        )
        list.add(
            Route(
                "Karwan Bazar",
                "7:25AM",
                "Hatirjheel Rail Gate",
                "ChIJu4yI75m4VTcRobgICzaj7cQ",
                "23.750184", "90.393907"
            )
        )
        list.add(
            Route(
                "AUST",
                "7:45AM",
                "Ahsanullah University of Science and Technology",
                "ChIJRfi17H3HVTcRkneGOy_d6sI",
                "23.763879", "90.406258"
            )
        )

        updateBusRoute(busName, busTime, list)
    }

    fun pushAllBusInfo() {
        val map: HashMap<String, ArrayList<BusTiming>> = HashMap()

        var l: ArrayList<BusTiming> = ArrayList()
        l.add(BusTiming("6:30AM", "3:45PM"))
        l.add(BusTiming("8:30AM", "6:10PM"))
        map["Jamuna"] = l

        l = ArrayList()

        l.add(BusTiming("6:45AM", "3:45PM"))
        l.add(BusTiming("9:00AM", "6:10PM"))
        map["Shurma"] = l

        l = ArrayList()
        l.add(BusTiming("6:00AM", "3:45PM"))
        l.add(BusTiming("8:30AM", "6:10PM"))
        map["Kornofuli"] = l

        l = ArrayList()
        l.add(BusTiming("6:15AM", "3:45PM"))
        l.add(BusTiming("8:30AM", "6:10PM"))
        map["Kapotakkho"] = l

        l = ArrayList()
        l.add(BusTiming("6:00AM", "3:45PM"))
        l.add(BusTiming("8:30AM", "6:10PM"))
        map["Padma"] = l

        l = ArrayList()
        l.add(BusTiming("6:15AM", "3:45PM"))
        l.add(BusTiming("8:30AM", "6:10PM"))
        map["Meghna"] = l

        updateAvailableBusInfo(map)
    }

    private fun updateAvailableBusInfo(list: HashMap<String, ArrayList<BusTiming>>) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("availableBusInfo")
        myRef.setValue(list)
    }

    private fun updateBusRoute(busName: String, busTime: String, list: ArrayList<Route>) {
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("bus/$busName/$busTime/routes")
        myRef.setValue(list)
    }

}