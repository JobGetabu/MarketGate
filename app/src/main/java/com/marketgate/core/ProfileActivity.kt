package com.marketgate.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.models.USER_AGENT
import com.marketgate.models.USER_FARMER
import com.marketgate.models.UserAgent
import com.marketgate.models.UserFarmer
import com.marketgate.utils.showAlert
import com.raiachat.util.loadUrl
import kotlinx.android.synthetic.main.activity_profile_farmer.*

class ProfileActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProfileActivity"
        const val USER_ID_EXTRA = "USER_ID_EXTRA"


        fun newIntent(context: Context): Intent =
            Intent(context, ProfileActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_farmer)

        val userId = intent.getStringExtra(USER_ID_EXTRA)

        setUI(userId)
        setUI2(userId)
    }

    private fun setUI(userId: String?) {
        FirebaseFirestore.getInstance().collection(USER_FARMER).document(userId!!).get()
            .addOnSuccessListener {
                if (!it.exists()) return@addOnSuccessListener
                val user: UserFarmer = it.toObject(UserFarmer::class.java) ?: return@addOnSuccessListener
                p_profile.loadUrl(user.photourl)
                p_profilename.text = user.name
            }
    }

    private fun setUI2(userId: String?) {
        FirebaseFirestore.getInstance().collection(USER_AGENT).document(userId!!).get()
            .addOnSuccessListener {
                if (!it.exists()) return@addOnSuccessListener
                val user: UserAgent = it.toObject(UserAgent::class.java) ?: return@addOnSuccessListener
                p_profile.loadUrl(user.photourl)
                p_profilename.text = user.name
            }
    }

    fun onFabClick(v: View) {
        showAlert(this, "Success", "Product added to watchlist")
    }
}
