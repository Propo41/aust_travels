package com.pixieium.austtravels.auth

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivityForgotPasswordBinding


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

    private fun hideKeyboard() {
        // hide the keyboard
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
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
            hideKeyboard()
            mBinding.sendBtn.isEnabled = false
            mBinding.eduMail.editText?.text?.clear()
            mBinding.progressBar.visibility = View.VISIBLE
            // email is valid.
            FirebaseAuth.getInstance().sendPasswordResetEmail(text)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "An email is sent to your institutional email",
                            Toast.LENGTH_SHORT
                        ).show()
                        mBinding.progressBar.visibility = View.GONE
                        mBinding.sendBtn.isEnabled = true
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        it.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.sendBtn.isEnabled = true
                }
        }
    }

    fun onReturnClick(view: View) {
        finish()
    }
}