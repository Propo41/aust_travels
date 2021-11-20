package com.pixieium.austtravels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.view.View;

import com.pixieium.austtravels.databinding.ActivityHomeBinding;
import com.pixieium.austtravels.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mBinding.signin.setOnClickListener(view1 -> {
            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });


    }
}