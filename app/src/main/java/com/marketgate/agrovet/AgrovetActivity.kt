package com.marketgate.agrovet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marketgate.R

class AgrovetActivity : AppCompatActivity() {

    companion object {
        private const val HOME = "Farmer"
        private const val PRODUCT = "Product"
        private const val AGENCY = "Agency"
        private const val NEWS = "News"
        private const val PROFILE = "Profile"

        fun newIntent(context: Context): Intent =
            Intent(context, AgrovetActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agrovet)
    }
}
