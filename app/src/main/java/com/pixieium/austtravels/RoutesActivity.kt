package com.pixieium.austtravels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pixieium.austtravels.models.Route
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.pixieium.austtravels.databinding.ActivityRoutesBinding


class RoutesActivity : AppCompatActivity() {
    private lateinit var mAdapter: RoutesAdapter
    private lateinit var mLayoutManager
            : RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBinding: ActivityRoutesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes)

        mBinding = ActivityRoutesBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)


        val routeList: ArrayList<Route> = ArrayList()
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan Munshijagc", "6:45 AM", "Kalabagan", "123asd"))
        routeList.add(Route("Kalabagan", "6:45 AM", "Kalabagan", "123asd"))

        initRecyclerView(routeList)
        initSpinnerName()
        initSpinnerTime()

    }

    private fun initSpinnerName() {
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, items)
        (mBinding.time.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initSpinnerTime() {
        val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        val adapter = ArrayAdapter(baseContext, R.layout.item_spinner, items)
        (mBinding.name.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initRecyclerView(routeList: ArrayList<Route>) {
        mRecyclerView = findViewById(R.id.recyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = RoutesAdapter(routeList)
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }
}