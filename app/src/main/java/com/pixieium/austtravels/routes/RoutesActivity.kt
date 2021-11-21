package com.pixieium.austtravels.routes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixieium.austtravels.models.Route
import androidx.recyclerview.widget.RecyclerView
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityLiveTrackBinding
import com.pixieium.austtravels.databinding.ActivityRoutesBinding
import com.pixieium.austtravels.models.BusInfo
import androidx.lifecycle.lifecycleScope
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

        val routeList: ArrayList<Route> = ArrayList()
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan Munshijagc", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))

        // initRecyclerView(routeList)

        mBinding.signin.setOnClickListener {
            mDatabase.fetchRouteList("Jamuna", "6:30AM")
        }

        lifecycleScope.launch {
            val list: ArrayList<BusInfo> = mDatabase.fetchAllBusInfo()
            initSpinnerName(list)
            initSpinnerTime(list)
        }

    }

    private fun pushBusRouteInfo() {
        val list: ArrayList<BusInfo> = ArrayList()


    }

    private fun pushAllBusInfo() {
        val list: ArrayList<BusInfo> = ArrayList()
        list.add(BusInfo("Jamuna", "6:30AM"))
        list.add(BusInfo("Jamuna", "8:30AM"))

        list.add(BusInfo("Shurma", "6:45AM"))
        list.add(BusInfo("Shurma", "9:00AM"))

        list.add(BusInfo("Kornofuli", "6:00AM"))
        list.add(BusInfo("Kornofuli", "8:30AM"))

        list.add(BusInfo("Kapotakkho", "6:15AM"))
        list.add(BusInfo("Kapotakkho", "8:30AM"))

        list.add(BusInfo("Padma", "6:30AM"))
        list.add(BusInfo("Padma", "8:30AM"))

        list.add(BusInfo("Meghna", "6:15AM"))
        list.add(BusInfo("Meghna", "8:30AM"))

        mDatabase.updateAvailableBusInfo(list)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            // todo logout()
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
    }

    private fun initSpinnerTime(list: ArrayList<BusInfo>) {
        val items: ArrayList<String> = ArrayList()
        for (busInfo: BusInfo in list) {
            items.add(busInfo.time)
        }
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, items)
        (mBinding.time.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initRecyclerView(routeList: ArrayList<Route>) {
        mRecyclerView = findViewById(R.id.recyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = RoutesAdapter(routeList)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }
}