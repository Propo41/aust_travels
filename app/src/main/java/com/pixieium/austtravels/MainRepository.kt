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

    fun pushBusRouteInfo1(busName: String, busTime: String) {
        val list: ArrayList<Route> = ArrayList()

        list.add(
            Route(
                "Mohammadpur",
                "8:30AM",
                "Mohammadpur Bus Stand",
                "23.757056",
                "90.361334"
            )
        )
        list.add(
            Route(
                "Shankar",
                "8:37AM",
                "Chhayanaut Shangskriti-Bhavan",
                "23.750767",
                "90.368380"
            )
        )
        list.add(
            Route(
                "Dhanmondi 15",
                "8:43AM",
                "15 No Bus Stand",
                "23.744144",
                "90.372813"
            )
        )
        list.add(
            Route(
                "Jigatola", "8:50AM", "Zigatola Bus Stand",
                "23.739193", "90.375611"
            )
        )

        list.add(
            Route(
                "City College",
                "8:56AM",
                "City College Bus Stop",
                "23.739430", "90.383068"
            )
        )
        list.add(
            Route(
                "Kalabagan",
                "9:05AM",
                "Kalabagan Bus Stoppage",
                "23.747743", "90.380171"
            )
        )
        list.add(
            Route(
                "Rasel Square",
                "9:10AM",
                "Dhanmondi 32 Bus Stop",
                "23.751599", "90.377880"
            )
        )

        list.add(
            Route(
                "Panthapath Signal",
                "9:20AM",
                "Panthapath Signal",
                "23.751230", "90.386885"
            )
        )

        list.add(
            Route(
                "Karwan Bazar",
                "9:25AM",
                "Kawran Bazar Bot Tala Bus Stop",
                "23.750708", "90.395648"
            )
        )
        list.add(
            Route(
                "AUST",
                "10:00AM",
                "Ahsanullah University of Science and Technology",
                "23.763879", "90.406258"
            )
        )

        updateBusRoute(busName, busTime, list)
    }

    fun pushBusRouteInfo2(busName: String, busTime: String) {
        val list: ArrayList<Route> = ArrayList()

        list.add(
            Route(
                "Mohammadpur",
                "6:45AM",
                "Mohammadpur Bus Stand",
                "23.757056",
                "90.361334"
            )
        )
        list.add(
            Route(
                "Shankar",
                "6:50AM",
                "Chhayanaut Shangskriti-Bhavan",
                "23.750767",
                "90.368380"
            )
        )
        list.add(
            Route(
                "Dhanmondi 15 Unimart",
                "6:55AM",
                "15 No Bus Stand",
                "23.744144",
                "90.372813"
            )
        )
        list.add(
            Route(
                "Jigatola", "7:00AM", "Zigatola Bus Stand",
                "23.739193", "90.375611"
            )
        )

        list.add(
            Route(
                "Nilkhet", "7:08AM", "Nilkhet Mor",
                "23.732880", "90.384998"
            )
        )

        list.add(
            Route(
                "New Market", "7:10AM", "New market Balaka Foot Over Bridge",
                "23.733011", "90.384774"
            )
        )

        list.add(
            Route(
                "City College",
                "7:15AM",
                "City College Bus Stop",
                "23.739466", "90.383225"
            )
        )
        list.add(
            Route(
                "Kalabagan",
                "7:18AM",
                "Kalabagan Bus Stoppage",
                "23.747743", "90.380171"
            )
        )
        list.add(
            Route(
                "Rasel Square",
                "7:22AM",
                "Dhanmondi 32 Bus Stop",
                "23.751599", "90.377880"
            )
        )

        list.add(
            Route(
                "Square Hospital",
                "7:25AM",
                "Square Hospital Ltd",
                "23.752738", "90.381539"
            )
        )

        list.add(
            Route(
                "Panthapath Signal",
                "7:30AM",
                "Panthapath Signal",
                "23.751092", "90.387271"
            )
        )

        list.add(
            Route(
                "Karwan Bazar",
                "7:32AM",
                "Kawran Bazar Bot Tala Bus Stop",
                "23.750708", "90.395648"
            )
        )
        list.add(
            Route(
                "AUST",
                "7:45AM",
                "Ahsanullah University of Science and Technology",
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