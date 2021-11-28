package com.pixieium.austtravels.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixieium.austtravels.databinding.ActivitySignInBinding
import com.pixieium.austtravels.home.HomeActivity
import java.lang.Exception

class SignInActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignInBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        mBinding.signup.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
        }
        mBinding.signin.setOnClickListener {
            try {
                // hide the keyboard
                hideKeyboard()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            } finally {
                signInUserByEmail()
            }
        }
    }

    private fun hideKeyboard() {
        // hide the keyboard
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.root.windowToken, 0)
    }

    private fun signInUserByEmail() {
        val email = mBinding.email.editText!!.text.toString()
        val password = mBinding.password.editText!!.text.toString()
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)) {
                mBinding.email.error = "Email cannot be empty"
            }
            if (TextUtils.isEmpty(password)) {
                mBinding.password.error = "Password cannot be empty"
            }
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        checkVerifiedEmail()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkVerifiedEmail() {
        val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if (currentUser?.isEmailVerified == true) {
            val intent = Intent(baseContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            val snakbar: Snackbar = Snackbar.make(
                mBinding.signinLayout,
                "Please verify your email",
                Snackbar.LENGTH_LONG
            )
                .setAction("Resend email", View.OnClickListener {
                    currentUser?.sendEmailVerification()?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(
                                this@SignInActivity,
                                "Email verification link sent to your email",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
                })

            snakbar.show()

            Firebase.auth.signOut()
        }
    }
}