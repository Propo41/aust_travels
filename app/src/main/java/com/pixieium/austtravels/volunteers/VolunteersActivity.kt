package com.pixieium.austtravels.volunteers

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityVolunteersBinding
import com.pixieium.austtravels.models.Volunteer
import com.pixieium.austtravels.settings.SettingsActivity
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
            val volunteers: ArrayList<Volunteer> = mDatabase.fetchVolunteers()
            if (volunteers.size > 0) {
                mBinding.notFoundPlaceholder.visibility = View.GONE
                mBinding.topPosition.visibility = View.VISIBLE
                mBinding.firstPosition.loadSvg(volunteers[0].userImage)
                mBinding.firstTime.text = volunteers[0].totalContributionFormatted
                mBinding.topNameTv.text = volunteers[0].name
                mBinding.topRollTv.text = volunteers[0].universityId

                volunteers.removeAt(0)
                initRecyclerView(volunteers)
            } else {
                mBinding.notFoundPlaceholder.visibility = View.VISIBLE
                mBinding.topPosition.visibility = View.GONE
            }
        }

    }

    /**
     * By default, ImageViews don't support SVG formats.
     * So, instead we are using the coil library to render svg files
     */
    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(2)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    private fun initRecyclerView(volunteers: ArrayList<Volunteer>) {
        mRecyclerView = findViewById(R.id.vrecyclerView)
        mLayoutManager = LinearLayoutManager(this)
        mAdapter = VolunteerAdapter(volunteers)
        mBinding.vrecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter
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
}