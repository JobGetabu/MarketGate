package com.marketgate.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marketgate.R

class AdminActivity : AppCompatActivity() {

    companion object {
        private const val HOME = "Farmer"
        private const val PRODUCT = "Product"
        private const val AGENCY = "Agency"
        private const val NEWS = "News"
        private const val PROFILE = "Profile"

        fun newIntent(context: Context): Intent =
            Intent(context, AdminActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        launchActivity(ListMultiSelection::class.java)
        finish()
    }

    private fun launchActivity(intentClass: Class<*>) {

        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(com.marketgate.R.anim.fade_out, com.marketgate.R.anim.fade_in)
    }
}
