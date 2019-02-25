package com.marketgate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        String name = getApplicationContext().getPackageName();
        SharedPreferences settings = getSharedPreferences(name, Context.MODE_PRIVATE);

        boolean firstRun = settings.getBoolean("firstRun", true);

        if (firstRun)//if running for first time
        //Intro will load for first time
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

        }

        Intent i = new Intent(SplashActivity.this, FarmerActivity.class);//introActivity
        startActivity(i);
        finish();
    }
}