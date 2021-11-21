package com.pixieium.austtravels.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pixieium.austtravels.R
import com.pixieium.austtravels.databinding.ActivitySignupBinding
import com.pixieium.austtravels.models.UserInfo
import kotlinx.coroutines.launch
import java.util.ArrayList
import android.content.Intent
import com.pixieium.austtravels.home.HomeActivity


class SignUpActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySignupBinding
    private val mDatabase: AuthRepository = AuthRepository()
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view: View = mBinding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            val semesters: ArrayList<String> = mDatabase.getSemesterInfo()
            val departments: ArrayList<String> = mDatabase.getSemesterInfo()

            initSpinnerSemester(semesters)
            initSpinnerDepartment(departments)
        }

        FirebaseDatabase.getInstance().getReference("Demo").child("data").setValue("value")
        mBinding.signup.setOnClickListener { createNewUser() }
    }

    private fun createNewUser() {
        val email = mBinding.eduMail.editText!!.text.toString()
        val password = mBinding.password.editText!!.text.toString()
        val userName = mBinding.name.editText!!.text.toString()
        val semester = mBinding.semester.editText!!.text.toString()
        val department = mBinding.semester.editText!!.text.toString()
        val universityId = mBinding.universityId.editText!!.text.toString()

        if (!isInputValid()) {
            Toast.makeText(this, "Please enter your information correctly", Toast.LENGTH_SHORT)
                .show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        if (user != null) {
                            val userInfo =
                                UserInfo(
                                    email,
                                    userName,
                                    semester,
                                    department,
                                    universityId
                                )
                            lifecycleScope.launch {
                                if (mDatabase.createNewUser(userInfo, user.uid)) {
                                    val intent =
                                        Intent(this@SignUpActivity, HomeActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Something went wrong! Couldn't create your profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d("signUp", task.exception.toString())
                        task.exception?.printStackTrace()
                        Toast.makeText(
                            applicationContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun isInputValid(): Boolean {
        val email = mBinding.eduMail.editText!!.text.toString()
        val password = mBinding.password.editText!!.text.toString()
        val userName = mBinding.name.editText!!.text.toString()
        val semester = mBinding.semester.editText!!.text.toString()
        val department = mBinding.semester.editText!!.text.toString()
        val universityId = mBinding.universityId.editText!!.text.toString()

        if (email.split('@')[1] != "aust.edu") {
            println(email.split('@')[1])
            mBinding.eduMail.error = "You must enter your institutional mail"
            return false
        }

        if (TextUtils.isEmpty(email)) {
            mBinding.eduMail.error = "Field is required"
            return false
        }

        if (TextUtils.isEmpty(universityId)) {
            mBinding.universityId.error = "Field is required"
            return false
        }

        if (TextUtils.isEmpty(password)) {
            mBinding.password.error = "Field is required"
            return false
        }
        if (TextUtils.isEmpty(userName)) {
            mBinding.name.error = "Field is required"
            return false
        }
        if (TextUtils.isEmpty(semester)) {
            mBinding.semester.error = "Field is required"
            return false
        }
        if (TextUtils.isEmpty(department)) {
            mBinding.department.error = "Field is required"
            return false
        }

        return true
    }

    private fun initSpinnerSemester(items: ArrayList<String>) {
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.item_spinner, items)
        mBinding.semesterDropdown.setAdapter(arrayAdapter)
    }

    private fun initSpinnerDepartment(items: ArrayList<String>) {
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.item_spinner, items)
        mBinding.departmentDropdown.setAdapter(arrayAdapter)
    }
}