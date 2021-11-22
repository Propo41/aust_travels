package com.pixieium.austtravels.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.pixieium.austtravels.databinding.ActivitySignInBinding
import com.pixieium.austtravels.home.HomeActivity

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
        mBinding.signin.setOnClickListener { signInUserByEmail() }
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
                        val intent = Intent(baseContext, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        //task.exception?.printStackTrace()
                        Toast.makeText(
                            applicationContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}