package com.pixieium.austtravels.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.pixieium.austtravels.R;
import com.pixieium.austtravels.databinding.ActivitySignInBinding;
import com.pixieium.austtravels.databinding.ActivitySignupBinding;
import com.pixieium.austtravels.home.HomeActivity;
import com.pixieium.austtravels.models.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding mBinding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        initSpinnerSemester();
        initSpinnerDepartment();

        FirebaseDatabase.getInstance().getReference("Demo").child("data").setValue("value");

        mBinding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });


    }


    private void createNewUser(){

        String email = mBinding.eduMail.getEditText().getText().toString();
        String password = mBinding.password.getEditText().getText().toString();
        String userName = mBinding.name.getEditText().getText().toString();
        String semester = mBinding.semester.getEditText().getText().toString();
        String department = mBinding.semester.getEditText().getText().toString();




        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)|| TextUtils.isEmpty(userName)||TextUtils.isEmpty(semester)||TextUtils.isEmpty(department))
        {
            if(TextUtils.isEmpty(email))
            {
                mBinding.eduMail.setError("Email is empty");
            }

            if(TextUtils.isEmpty(password))
            {
                mBinding.password.setError("Email is empty");
            }

            if(TextUtils.isEmpty(userName))
            {
                mBinding.name.setError("Email is empty");
            }
            if(TextUtils.isEmpty(semester))
            {
                mBinding.semester.setError("Semester is empty");
            }
            if(TextUtils.isEmpty(department))
            {
              mBinding.department.setError("Department is empty");
            }
        }

        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null)
                                {
                                    UserInfo userInfo = new UserInfo(email,userName,semester,department);
                                    FirebaseDatabase.getInstance().getReference("users")
                                            .child(user.getUid())
                                            .setValue(userInfo).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("fails",e.toString());
                                        }
                                    });
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful())
//                                            {
//                                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                                                startActivity(intent);
//                                            }
//                                        }
//                                    });

                                }



                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("signUp",task.getException().toString());

                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                task.getException().printStackTrace();

                            }
                        }
                    });
        }

    }

    private void initSpinnerSemester()
    {
        ArrayAdapter<String>arrayAdapter;
        ArrayList<String> items = new ArrayList<String>(){{
            add("5.2");
            add("5.1");
            add("4.2");
            add("4.1");
            add("3.2");
            add("3.1");
            add("2.2");
            add("2.1");
            add("1.2");
            add("1.1");
        }};

        arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner,items);

        mBinding.semesterDropdown.setAdapter(arrayAdapter);


    }
    private void initSpinnerDepartment()
    {
        ArrayAdapter<String>arrayAdapter;
        ArrayList<String> items = new ArrayList<String>(){{
            add("CSE");
            add("EEE");
            add("CE");
            add("ME");
            add("IPE");
            add("TE");
            add("BBA");
            add("ARCH");
        }};

        arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner,items);

        mBinding.departmentDropdown.setAdapter(arrayAdapter);


    }
}