package com.marketgate.core

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.admin.AdminActivity
import com.marketgate.agent.AgentActivity
import com.marketgate.agrovet.AgrovetActivity
import com.marketgate.farmer.FarmerActivity
import com.marketgate.models.*
import com.marketgate.utils.LoaderDialogue
import com.marketgate.utils.PreferenceHelper
import com.marketgate.utils.PreferenceHelper.set
import com.raiachat.util.hideView
import com.raiachat.util.toast
import kotlinx.android.synthetic.main.activity_login_card_light.*

class SigninActivity : AppCompatActivity() {

    private lateinit var loader: LoaderDialogue
    private var TAG: String = "login"
    private lateinit var usertype: String

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_card_light)

        loader = LoaderDialogue()

        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        need_new_account.setOnClickListener {
            signin_label.text = "Sign up"
            siginTxtBtn.text = "Sign up"
            need_new_account.hideView()

        }
        siginTxtBtn.setOnClickListener {

            if (signin_label.text == "Sign In") {
                loginMe()
            } else {
                createAccount()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        val prefs = PreferenceHelper.customPrefs(this)

        usertype = prefs.getString(PreferenceHelper.PREF_USER_TYPE, "")
        if (usertype != "" && auth.currentUser != null) goToHome(usertype)
    }

    private fun dismissProgressDialog() {
        if (loader.isAdded && loader.isVisible)
            loader.dismiss()
    }

    private fun goToHome(usertype: String) {

        dismissProgressDialog()
        var intent: Intent? = null

        when (usertype) {
            "Farmer" -> {
                val prefs = PreferenceHelper.customPrefs(this)
                prefs[PreferenceHelper.PREF_USER_TYPE] = usertype
                intent = FarmerActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Agrovet" -> {
                val prefs = PreferenceHelper.customPrefs(this)
                prefs[PreferenceHelper.PREF_USER_TYPE] = usertype
                intent = AgrovetActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Agent" -> {
                val prefs = PreferenceHelper.customPrefs(this)
                prefs[PreferenceHelper.PREF_USER_TYPE] = usertype
                intent = AgentActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Admin" -> {
                val prefs = PreferenceHelper.customPrefs(this)
                prefs[PreferenceHelper.PREF_USER_TYPE] = usertype
                intent = AdminActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        startActivity(intent)
        finish()
    }

    private fun loginMe(){

        val emailTxt = email.text.toString()
        val pss = password.text.toString()

        if (emailTxt.isEmpty() && pss.isEmpty()) {
            toast("Missing email or password")
            return
        }

        showProgress("Logging you in")

        auth.signInWithEmailAndPassword(emailTxt, pss)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    dismissProgressDialog()

                    val prefs = PreferenceHelper.customPrefs(this)
                    usertype = prefs.getString(PreferenceHelper.PREF_USER_TYPE, "")
                    if (usertype != "" && auth.currentUser != null) goToHome(usertype)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    dismissProgressDialog()
                }
            }

    }

    private fun createAccount(){
        val emailTxt = email.text.toString()
        val pss = password.text.toString()

        if (emailTxt.isEmpty() && pss.isEmpty()) {
            toast("Missing email or password")
            return
        }

        showProgress("Creating a new account")

        auth.createUserWithEmailAndPassword(emailTxt, pss)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    dismissProgressDialog()
                    saveUser(user!!)
                    val prefs = PreferenceHelper.customPrefs(this)
                    usertype = prefs.getString(PreferenceHelper.PREF_USER_TYPE, "")
                    if (usertype != "" && auth.currentUser != null) goToHome(usertype)


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    dismissProgressDialog()
                }
            }
    }

    private fun showProgress(title: String = "", txt: String = "") {
        val b = Bundle()
        b.putString(LoaderDialogue.DIA_TITLE, title)
        b.putString(LoaderDialogue.DIA_TXT, txt)

        loader.arguments = b

        val tr = supportFragmentManager.beginTransaction()
        loader.show(tr, LoaderDialogue.TAG)
    }

    private fun saveUser(user: FirebaseUser) {
        when (usertype) {

            "Farmer" -> {
                firestore.collection(USER_FARMER).document(user!!.uid).get()
                    .addOnCompleteListener {
                        val result = it.result
                        if (!result!!.exists()) {

                            val userFarmer = UserFarmer(
                                user.displayName, true, false, false, true, "Market Gate",
                                "", user.email.toString(), "", null, "Nairobi CBD", null, user.uid
                            )

                            firestore.collection(USER_FARMER).document(user.uid).set(userFarmer)
                        }
                    }
            }
            "Agrovet" -> {
                firestore.collection(USER_AGROVET).document(user!!.uid).get()
                    .addOnCompleteListener {
                        val result = it.result
                        if (!result!!.exists()) {

                            val userAgrovet = UserAgrovet(
                                user.displayName, true, false, false, true, "Market Gate Agrovet",
                                user?.photoUrl.toString(), user.email.toString(), "", null, "Mombasa Near Hibernet", user.uid
                            )

                            firestore.collection(USER_AGROVET).document(user.uid).set(userAgrovet)
                        }
                    }
            }
            "Agent" -> {
                firestore.collection(USER_AGENT).document(user!!.uid).get()
                    .addOnCompleteListener {
                        val result = it.result
                        if (!result!!.exists()) {

                            val userAgent = UserAgent(
                                user.displayName, true, false, false, true, "Market Gate Agent",
                                user.photoUrl.toString(), user.email.toString(), "", null, "Nyeri Farm Near Tomcat", user.uid
                            )

                            firestore.collection(USER_AGENT).document(user.uid).set(userAgent)
                        }
                    }
            }
            "Admin" -> {
                firestore.collection(USER_ADMIN).document(user!!.uid).get()
                    .addOnCompleteListener {
                        val result = it.result
                        if (!result!!.exists()) {

                            val userAdmin = UserAdmin(
                               user.displayName, user.photoUrl.toString(), user.email.toString(), userid = user.uid
                            )

                            firestore.collection(USER_ADMIN).document(user.uid).set(userAdmin)
                        }
                    }
            }
        }
    }
}
