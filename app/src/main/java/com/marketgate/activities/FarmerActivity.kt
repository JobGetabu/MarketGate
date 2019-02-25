package com.marketgate.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marketgate.R

class FarmerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchActivity(LoginActivity::class.java)
    }

    private fun launchActivity(intentClass: Class<*>) {
        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in)
    }
}
