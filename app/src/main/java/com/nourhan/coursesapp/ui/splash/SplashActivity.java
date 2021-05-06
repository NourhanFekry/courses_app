package com.nourhan.coursesapp.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.nourhan.coursesapp.ui.MainActivity;
import com.nourhan.coursesapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                },
                3000);
    }
}