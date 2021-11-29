package com.pixieium.austtravels.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pixieium.austtravels.databinding.ActivityForgotPasswordBinding
import android.util.Patterns

import android.text.TextUtils
import android.widget.Toast
import com.pixieium.austtravels.R


class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target)
            .matches() && target.split("@").toTypedArray()[1] != "aust.edu"
    }

    fun onSendClick(view: View) {
        val text = mBinding.eduMail.editText?.text.toString()

        if (!isValidEmail(text)) {
            Toast.makeText(
                this,
                "Email is invalid. Please enter your institutional email",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // todo: do the forgot password backend logic
            // email is valid. Do the rest @fuad
        }
    }

    fun onReturnClick(view: View) {
        finish()
    }
}