package com.marketgate.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = null;

        SharedPreferences pref = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);

        if (pref.getBoolean("isFirstTimeLaunch", true)) {
            i = new Intent(SplashActivity.this, IntroActivity.class);//introActivity
            startActivity(i);
            finish();
        } else {
            i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }


    }
}