package com.pixieium.austtravels.routes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityRoutesBinding
import com.pixieium.austtravels.models.BusInfo
import com.pixieium.austtravels.models.BusTiming
import com.pixieium.austtravels.models.Representative
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.routes.adapters.RepresentativesAdapter
import com.pixieium.austtravels.routes.adapters.RoutesAdapter
import com.pixieium.austtravels.settings.SettingsActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class RoutesActivity : AppCompatActivity() {
    private lateinit var mAdapter: ConcatAdapter
    private lateinit var mRoutesAdapter: RoutesAdapter
    private lateinit var mLayoutManager
            : RecyclerView.LayoutManager
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

        mBinding.name.isEnabled = false
        mBinding.time.isEnabled = false
        mBinding.select.isEnabled = false

        lifecycleScope.launch {
            try {
                val list: ArrayList<BusInfo> = mDatabase.fetchAllBusInfo()
                if (list.size != 0) {
                    initSpinnerName(list)
                } else {
                    Toast.makeText(
                        baseContext,
                        "Couldn't fetch data from database. Please check your connection",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            } catch (e: Exception) {
                Timber.e(e, e.localizedMessage)
                Toast.makeText(
                    baseContext,
                    "Couldn't fetch data from database. Please check your connection",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        } else if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    private fun startLoading() {
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.select.isEnabled = false
    }

    private fun stopLoading() {
        mBinding.progressBar.visibility = View.GONE
        mBinding.select.isEnabled = true
    }


    private fun initSpinnerName(list: ArrayList<BusInfo>) {
        val items: ArrayList<String> = ArrayList()
        for (busInfo: BusInfo in list) {
            items.add(busInfo.name)
        }
        mBinding.name.isEnabled = true

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
                //Toast.makeText(this@RoutesActivity, s.toString(), Toast.LENGTH_SHORT).show()
                mBinding.time.editText?.text?.clear()
                initSpinnerTime(s.toString(), list)
                mBinding.select.isEnabled = true
            }
        })

    }

    private fun initSpinnerTime(selectedName: String, list: ArrayList<BusInfo>) {
        val timingList: ArrayList<String> = ArrayList()
        mBinding.time.isEnabled = true
        for (busInfo: BusInfo in list) {
            if (busInfo.name == selectedName) {
                for (timing: BusTiming in busInfo.timing) {
                    timingList.add("${timing.startTime} | ${timing.departureTime}")
                }
                break
            }
        }
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, timingList)
        (mBinding.time.editText as? AutoCompleteTextView)?.setAdapter(adapter)

    }

    /**
     * the str is assumed to be in the format: 6:30AM | 3:45PM
     * the function returns the first time 6:30AM
     */
    private fun parseDeptTime(str: String): String {
        return str.split('|')[0].trim()
    }

    private fun initRecyclerView(routeList: ArrayList<Route>) {
        mLayoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            val list = mDatabase.getBusRepresentativeInfo(mBinding.name.editText?.text.toString())
            val mRepAdapter = RepresentativesAdapter(list)
            mRoutesAdapter = RoutesAdapter(routeList)

            mAdapter = ConcatAdapter(mRoutesAdapter, mRepAdapter)
            mBinding.recyclerView.layoutManager = mLayoutManager
            mBinding.recyclerView.adapter = mAdapter
        }

    }

    fun onBusSelectClick(view: View) {
        if (!mBinding.time.editText?.text.isNullOrEmpty() && !mBinding.name.editText?.text.isNullOrEmpty()) {
            startLoading()

            lifecycleScope.launch {
                val selectedBusTime = parseDeptTime(mBinding.time.editText?.text.toString())

                val list: ArrayList<Route> = mDatabase.fetchRouteList(
                    mBinding.name.editText?.text.toString(),
                    selectedBusTime
                )
                if (list.size == 0) {
                    Toast.makeText(this@RoutesActivity, "Route not added yet!", Toast.LENGTH_SHORT)
                        .show()
                    mBinding.placeholder.visibility = View.VISIBLE
                    mBinding.recyclerView.visibility = View.GONE
                } else {
                    mBinding.placeholder.visibility = View.GONE
                    initRecyclerView(list)
                    mBinding.recyclerView.visibility = View.VISIBLE
                }
                stopLoading()
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