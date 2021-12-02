package com.pixieium.austtravels.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.pixieium.austtravels.R
import com.pixieium.austtravels.auth.SignInActivity
import com.pixieium.austtravels.databinding.ActivitySettingsBinding
import com.pixieium.austtravels.models.UserSettings
import com.pixieium.austtravels.settings.dialog.*
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity(), PromptVolunteerDialog.FragmentListener,
    ReAuthenticateDialog.FragmentListener, DeleteConfirmationDialog.FragmentListener,
    PromptVolunteerInfoDialog.FragmentListener, SelectBusDialog.FragmentListener {
    private val mDatabase: SettingsRepository = SettingsRepository()
    private lateinit var mUid: String
    private lateinit var mBinding: ActivitySettingsBinding
    private var mUserSettings: UserSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
        setSupportActionBar(mBinding.topAppBar)

        val isVolunteer = intent.getBooleanExtra("IS_VOLUNTEER", false)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mUid = Firebase.auth.currentUser?.uid.toString()

        lifecycleScope.launch {
            mUserSettings = mDatabase.getUserSettings(mUid)
            if (mUserSettings == null) {
                mUserSettings = UserSettings()
                Toast.makeText(
                    this@SettingsActivity,
                    "Something went wrong. Couldn't fetch your settings from the server!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            updateUi(isVolunteer)
        }

        attachListeners()
    }

    private fun updateUi(isVolunteer: Boolean?) {
        try {
            if (isVolunteer != null && !isVolunteer) {
                mBinding.pingNotificationSwitch.visibility = View.GONE
            } else {
                mBinding.becomeVolunteerBtn.visibility = View.GONE
                mBinding.pingNotificationSwitch.isChecked = mUserSettings?.pingNotification == true
                mBinding.locationNotificationSwitch.isChecked =
                    mUserSettings?.locationNotification == true
                mBinding.primaryBusVal.text =
                    getString(R.string.primary_bus_value, mUserSettings?.primaryBus)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@SettingsActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * called after user changes primary Bus
     */
    private fun updatePingSubscription(primaryBus: String?) {
        val xx = mBinding.pingNotificationSwitch.isChecked

        if (!xx) {
            // if the ping notifications are disabled, then un-subscribe the user
            primaryBus?.let {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(it).addOnSuccessListener {
                    Log.d("unsubscribeFromTopic -", primaryBus)
                }
                Snackbar.make(
                    mBinding.root,
                    "You will now stop receiving any ping notifications for $primaryBus",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            // subscribe the user to bus notification
            primaryBus?.let {
                // Show for the first time
                FirebaseMessaging.getInstance().subscribeToTopic(it).addOnSuccessListener {
                    Snackbar.make(
                        mBinding.root,
                        "You will now receive ping notifications from $primaryBus whenever someone pings you.",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    /**
     * attaches listeners for pingNotification and locationNotification switches
     */
    private fun attachListeners() {
        // To listen for a switch's checked/unchecked state changes
        mBinding.pingNotificationSwitch.setOnClickListener {
            val xx = mBinding.pingNotificationSwitch.isChecked
            mDatabase.updatePingNotificationSettings(mUid, xx)
            updatePingSubscription(mUserSettings?.primaryBus)
        }

        mBinding.locationNotificationSwitch.setOnClickListener {
            // if no bus is currently selected, show a prompt
            if (mUserSettings?.primaryBus == "None") {
                Toast.makeText(
                    this@SettingsActivity,
                    "You must choose a primary bus first before subscribing to notifications",
                    Toast.LENGTH_SHORT
                ).show()
                mBinding.locationNotificationSwitch.isChecked = false
            } else {
                val xx = mBinding.locationNotificationSwitch.isChecked
                mDatabase.updateLocationNotificationSettings(mUid, xx)
                if (xx) {
                    // todo: subscribe user

                } else {
                    // todo: un-subscribe user
                }
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return false
    }

    fun onDeleteClick(view: View) {
        // confirmation dialog
        DeleteConfirmationDialog.newInstance()
            .show(supportFragmentManager, DeleteConfirmationDialog.TAG)
    }

    override fun onDeleteConfirmClick() {
        // re-verify user before deleting
        ReAuthenticateDialog.newInstance()
            .show(supportFragmentManager, ReAuthenticateDialog.TAG)
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
        PromptVolunteerInfoDialog.newInstance()
            .show(supportFragmentManager, PromptVolunteerInfoDialog.TAG)
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

    override fun onVolunteerConfirmClick(busName: String, contact: String) {
        lifecycleScope.launch {
            val payload = mDatabase.createVolunteer(mUid, busName, contact)
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

    fun onPrimaryBusChange(view: View) {
        SelectBusDialog.newInstance()
            .show(supportFragmentManager, SelectBusDialog.TAG)
    }

    fun onContributorsClick(view: View) {}

    override fun onBusSelectClick(selectedBusName: String) {
        mDatabase.updatePrimaryBus(mUid, selectedBusName)
        mUserSettings?.primaryBus = selectedBusName
        mBinding.primaryBusVal.text = getString(R.string.primary_bus_value, selectedBusName)
        updatePingSubscription(selectedBusName)
    }


    companion object {
        private const val TAG = "SettingsActivity"
    }
}


