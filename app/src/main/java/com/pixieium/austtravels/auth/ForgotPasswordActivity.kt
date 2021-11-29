package com.pixieium.austtravels.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pixieium.austtravels.databinding.ActivityForgotPasswordBinding
import android.util.Patterns

import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
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
            .matches()
    }


    private fun isEmailCorrect(target: CharSequence): String? {
        if (!isValidEmail(target)) {
            return "Please enter a valid email"
        } else {
            if (target.split("@").toTypedArray()[1] != "aust.edu") {
                return "You must enter your institutional mail"
            }
        }
        return null
    }


    fun onSendClick(view: View) {
        val text = mBinding.eduMail.editText?.text.toString()
        val xx = isEmailCorrect(text)
        if (xx != null) {
            Toast.makeText(
                this,
                xx,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // todo: do the forgot password backend logic
            // email is valid. Do the rest @fuad
            FirebaseAuth.getInstance().sendPasswordResetEmail(text)
                .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "An email is sent to your institutional email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    fun onReturnClick(view: View) {
        finish()
    }
}