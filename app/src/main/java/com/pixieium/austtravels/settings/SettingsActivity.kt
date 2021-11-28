package com.pixieium.austtravels.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivityHomeBinding
import com.pixieium.austtravels.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity(), PromptVolunteerDialog.FragmentListener,
    DeleteConfirmationDialog.FragmentListener {
    private val mDatabase: SettingsRepository = SettingsRepository()
    private lateinit var mUid: String
    private lateinit var mBinding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
        setSupportActionBar(mBinding.topAppBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mUid = Firebase.auth.currentUser?.uid.toString()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    fun onDeleteClick(view: View) {
        // re-verify user before deleting
        DeleteConfirmationDialog.newInstance()
            .show(supportFragmentManager, DeleteConfirmationDialog.TAG)
    }

    fun onPrivacyClick(view: View) {}

    fun onBecomeVolunteerClick(view: View) {
        PromptVolunteerDialog.newInstance()
            .show(supportFragmentManager, PromptVolunteerDialog.TAG)
    }

    fun onLogoutClick(view: View) {
        Toast.makeText(this, "Signing out!", Toast.LENGTH_SHORT).show()
        logout()
    }

    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onVolunteerApprovalClick() {
        lifecycleScope.launch {
            val payload = mDatabase.createVolunteer(mUid)
            if (payload.isStatus) {
                Toast.makeText(
                    this@SettingsActivity,
                    "We've received your request and will shortly review it.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@SettingsActivity,
                    payload.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onEnterPassword(password: String) {
        lifecycleScope.launch {
            if (mDatabase.deleteUser(password)) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Your account has been deleted! Redirecting...",
                    Toast.LENGTH_SHORT
                ).show()
                logout()
            } else {
                Toast.makeText(
                    this@SettingsActivity,
                    "Couldn't delete your account at the moment. Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


