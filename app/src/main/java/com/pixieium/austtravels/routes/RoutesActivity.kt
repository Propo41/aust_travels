package com.pixieium.austtravels.routes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixieium.austtravels.models.Route
import androidx.recyclerview.widget.RecyclerView
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityLiveTrackBinding
import com.pixieium.austtravels.databinding.ActivityRoutesBinding
import com.pixieium.austtravels.models.BusInfo
import androidx.lifecycle.lifecycleScope
import com.pixieium.austtravels.models.BusTiming
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RoutesActivity : AppCompatActivity() {
    private lateinit var mAdapter: RoutesAdapter
    private lateinit var mLayoutManager
            : RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBinding: ActivityRoutesBinding
    private val mDatabase: RoutesRepository = RoutesRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRoutesBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        lifecycleScope.launch {
            val list: ArrayList<BusInfo> = mDatabase.fetchAllBusInfo()
            mBinding.time.isEnabled = false
            initSpinnerName(list)
        }

        pushBusRouteInfo("Jamuna", "6:30AM")
        //pushAllBusInfo()

    }

    private fun pushBusRouteInfo(busName: String, busTime: String) {
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

        mDatabase.updateBusRoute(busName, busTime, list)
    }

    private fun pushAllBusInfo() {
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

        mDatabase.updateAvailableBusInfo(map)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Firebase.auth.signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    private fun initSpinnerName(list: ArrayList<BusInfo>) {
        val items: ArrayList<String> = ArrayList()
        for (busInfo: BusInfo in list) {
            items.add(busInfo.name)
        }
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, items)
        (mBinding.name.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        mBinding.name.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Toast.makeText(this@RoutesActivity, s.toString(), Toast.LENGTH_SHORT).show()
                initSpinnerTime(s.toString(), list)
            }
        })

    }

    private fun initSpinnerTime(selectedName: String, list: ArrayList<BusInfo>) {
        val timingList: ArrayList<String> = ArrayList()
        mBinding.time.isEnabled = true
        for (busInfo: BusInfo in list) {
            if (busInfo.name == selectedName) {
                for (timing: BusTiming in busInfo.timing) {
                    timingList.add(timing.startTime)
                }
                break
            }
        }
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, timingList)
        (mBinding.time.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initRecyclerView(routeList: ArrayList<Route>) {
        mRecyclerView = findViewById(R.id.recyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = RoutesAdapter(routeList)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }

    fun onBusSelectClick(view: View) {
        if (!mBinding.time.editText?.text.isNullOrEmpty() && !mBinding.name.editText?.text.isNullOrEmpty()) {
            lifecycleScope.launch {
                val list: ArrayList<Route> = mDatabase.fetchRouteList(
                    mBinding.name.editText?.text.toString(),
                    mBinding.time.editText?.text.toString()
                )
                initRecyclerView(list)
            }

        } else {
            Toast.makeText(
                this,
                "You must select a bus timing first",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}