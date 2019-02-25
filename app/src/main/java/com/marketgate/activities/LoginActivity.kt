package com.marketgate.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marketgate.utils.LoaderDialogue
import com.marketgate.utils.LoaderDialogue.DIA_TITLE
import com.marketgate.utils.LoaderDialogue.DIA_TXT
import kotlinx.android.synthetic.main.login_prompt.*


class LoginActivity : AppCompatActivity() {

    lateinit var loader: LoaderDialogue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.marketgate.R.layout.login_prompt)

        loader = LoaderDialogue()

        loginFarmer.setOnClickListener {
            showProgress("Login in...","Accessing farmers account")
        }
    }

    private fun launchActivity(intentClass: Class<*>) {
        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(com.marketgate.R.anim.fade_out, com.marketgate.R.anim.fade_in)
    }

    private fun showProgress(title: String = "",txt: String= ""){
        val b = Bundle()
        b.putString(DIA_TITLE, title)
        b.putString(DIA_TXT, txt)

        loader.setArguments(b)

        val tr = supportFragmentManager.beginTransaction()
        loader.show(tr,LoaderDialogue.TAG)
    }
}
