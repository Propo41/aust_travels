package com.pixieium.austtravels.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.pixieium.austtravels.utils.Constant
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class SettingsActivity : AppCompatActivity(), PromptVolunteerDialog.FragmentListener,
    ReAuthenticateDialog.FragmentListener, DeleteConfirmationDialog.FragmentListener,
    PromptVolunteerInfoDialog.FragmentListener, SelectBusDialog.FragmentListener {
    private val mDatabase: SettingsRepository = SettingsRepository()
    private lateinit var mUid: String
    private lateinit var mBinding: ActivitySettingsBinding
    private lateinit var mUserSettings: UserSettings

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

            Timber.d(mUserSettings.primaryBus)
            Timber.d(mUserSettings.locationNotification.toString())
            Timber.d(mUserSettings.pingNotification.toString())

            updateUi(isVolunteer)
        }

        attachListeners()
    }

    private fun updateUi(isVolunteer: Boolean?) {
        try {
            mBinding.locationNotificationSwitch.isChecked =
                mUserSettings.locationNotification == true

            mBinding.primaryBusVal.text =
                getString(R.string.primary_bus_value, mUserSettings.primaryBus)

            if (isVolunteer != null && !isVolunteer) {
                // if user is not a volunteer
                mBinding.pingNotificationContainer.visibility = View.GONE
                mBinding.becomeVolunteerBtn.visibility = View.VISIBLE
            } else {
                // if user is a volunteer
                // todo: add a button to stop being a volunteer
                mBinding.becomeVolunteerBtn.visibility = View.GONE
                mBinding.pingNotificationSwitch.isChecked = mUserSettings.pingNotification == true
                mBinding.pingNotificationContainer.visibility = View.VISIBLE
            }

        } catch (e: Exception) {
            Timber.e(e, e.localizedMessage)
            Toast.makeText(this@SettingsActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * called after user changes primary Bus
     */
    private fun updatePingSubscription(primaryBus: String?) {
        val isPingSwitchEnabled = mBinding.pingNotificationSwitch.isChecked

        if (!isPingSwitchEnabled) {
            // if the ping notifications are disabled, then un-subscribe the user
            if (mUserSettings.primaryBus != "None") {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mUserSettings.primaryBus)
                    .addOnSuccessListener {
                        Timber.d("unsubscribe from ping topic -", primaryBus)
                    }
                Snackbar.make(
                    mBinding.root,
                    "You will now stop receiving any ping notifications for $primaryBus",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        } else {
            // subscribe the user to bus notification
            // Show for the first time
            if (mUserSettings.primaryBus != "None") {
                FirebaseMessaging.getInstance().subscribeToTopic(mUserSettings.primaryBus)
                    .addOnSuccessListener {
                        Timber.d("subscribe to topic -", primaryBus)

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
            updatePingSubscription(mUserSettings.primaryBus)
        }

        mBinding.locationNotificationSwitch.setOnClickListener {
            // if no bus is currently selected, show a prompt
            if (mUserSettings.primaryBus == "None") {
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
                    subscribeUserLocationUpdates(mUserSettings.primaryBus)
                } else {
                    unSubscribeUserLocationUpdates()
                }
            }
        }
    }

    private fun unSubscribeUserLocationUpdates() {
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic("${mUserSettings.primaryBus}${Constant.USER_NOTIFY}")
            .addOnSuccessListener {
                Snackbar.make(
                    mBinding.root,
                    "You will now stop receiving any sorts of location updates",
                    Snackbar.LENGTH_LONG
                ).show()
            }
    }

    private fun subscribeUserLocationUpdates(primaryBus: String) {
        val isLocationSwitchEnabled = mBinding.locationNotificationSwitch.isChecked

        if (isLocationSwitchEnabled) {
            FirebaseMessaging.getInstance()
                .subscribeToTopic("$primaryBus${Constant.USER_NOTIFY}")
                .addOnSuccessListener {
                    Timber.d("Subscribed to location updates for bus: $primaryBus")
                    Snackbar.make(
                        mBinding.root,
                        "You will now receive notifications about $primaryBus whenever someone shares their location.",
                        Snackbar.LENGTH_LONG
                    ).show()
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

    fun onPrivacyClick(view: View) {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        startActivity(intent)
    }

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

        lifecycleScope.launch {
            try {
                // unsubscribe from ping notifications
                FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(mUserSettings.primaryBus).await()

                FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic("${mUserSettings.primaryBus}${Constant.USER_NOTIFY}")
                    .await()
            } catch (e: Exception) {
                Timber.e(e)
            }
            returnToSignIn()
        }
    }

    private fun returnToSignIn() {
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

    fun onContributorsClick(view: View) {
        Toast.makeText(this, "Under construction!", Toast.LENGTH_SHORT).show()
    }

    override fun onBusSelectClick(selectedBusName: String) {
        mDatabase.updatePrimaryBus(mUid, selectedBusName)
        mUserSettings.primaryBus = selectedBusName
        mBinding.primaryBusVal.text = getString(R.string.primary_bus_value, selectedBusName)
        updatePingSubscription(selectedBusName)
        subscribeUserLocationUpdates(selectedBusName)
        Toast.makeText(this, "Primary bus changed to $selectedBusName", Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val TAG = "SettingsActivity"
    }
}


