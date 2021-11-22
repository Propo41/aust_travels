package com.pixieium.austtravels.volunteers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityRoutesBinding
import com.pixieium.austtravels.databinding.ActivityVolunteersBinding
import com.pixieium.austtravels.models.Route
import com.pixieium.austtravels.models.Volunteer
import com.pixieium.austtravels.routes.RoutesAdapter

class VolunteersActivity : AppCompatActivity() {
    private lateinit var mAdapter: VolunteerAdapter
    private lateinit var mLayoutManager
            : RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBinding: ActivityVolunteersBinding
    lateinit var  list: ArrayList<Volunteer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVolunteersBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)
        setSupportActionBar(mBinding.toolbar)

        pushInfo()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun pushInfo() {
     list = ArrayList()

        list.add(
                Volunteer(
                       "abc",
                        "18",
                        "https://picsum.photos/200"


                )
        )
        list.add(
                Volunteer(
                       "abbc",
                        "04",
                        "https://picsum.photos/seed/picsum/200/300"
                )
        )

    }
    private fun initRecyclerView() {
        mRecyclerView = findViewById(R.id.vrecyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = VolunteerAdapter(list)
        mBinding.vrecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            Firebase.auth.signOut()
            Toast.makeText(this, "Signing out!", Toast.LENGTH_SHORT).show()
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
}