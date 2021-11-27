package com.pixieium.austtravels.volunteers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityVolunteersBinding
import com.pixieium.austtravels.models.UserInfo
import kotlinx.coroutines.launch

class VolunteersActivity : AppCompatActivity() {
    private lateinit var mAdapter: VolunteerAdapter
    private lateinit var mLayoutManager
            : RecyclerView.LayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mBinding: ActivityVolunteersBinding
    private val mDatabase: VolunteerRepository = VolunteerRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityVolunteersBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)
        setSupportActionBar(mBinding.toolbar)

        lifecycleScope.launch {
            val volunteers: ArrayList<UserInfo> = mDatabase.fetchVolunteers()
            initRecyclerView(volunteers)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun initRecyclerView(volunteers: ArrayList<UserInfo>) {
        mRecyclerView = findViewById(R.id.vrecyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = VolunteerAdapter(volunteers)
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