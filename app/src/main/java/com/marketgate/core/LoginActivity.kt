package com.marketgate.core

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.admin.AdminActivity
import com.marketgate.agent.AgentActivity
import com.marketgate.farmer.FarmerActivity
import com.marketgate.models.*
import com.marketgate.utils.GoogleLoginCallback
import com.marketgate.utils.GoogleSignInApiUtils.getUserData
import com.marketgate.utils.LoaderDialogue
import com.marketgate.utils.LoaderDialogue.DIA_TITLE
import com.marketgate.utils.LoaderDialogue.DIA_TXT
import com.marketgate.utils.PreferenceHelper
import com.marketgate.utils.PreferenceHelper.PREF_USER_TYPE
import com.marketgate.utils.PreferenceHelper.customPrefs
import com.marketgate.utils.PreferenceHelper.set
import com.marketgate.utils.showAlert
import kotlinx.android.synthetic.main.login_prompt.*


class LoginActivity : AppCompatActivity(), GoogleLoginCallback {

    private lateinit var loader: LoaderDialogue
    private var TAG: String = "login"
    private lateinit var usertype: String

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override val googleApiClient: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    override val googleSingInClient: GoogleSignInClient by lazy { GoogleSignIn.getClient(this, googleApiClient) }

    override fun onStart() {
        super.onStart()
        val prefs = PreferenceHelper.customPrefs(this)

        usertype = prefs.getString(PREF_USER_TYPE, "")
        if (usertype != "" && auth.currentUser != null) goToHome(usertype)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.marketgate.R.layout.login_prompt)

        loader = LoaderDialogue()

        //firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        loginFarmer.setOnClickListener {
            showProgress("Login in...", "Accessing farmers account")
            usertype = "Farmer"
            logInWithGoogle(this)
        }

        loginAgent.setOnClickListener {
            showProgress("Login in...", "Accessing agent account")
            usertype = "Agent"
            val prefs = customPrefs(this)
            prefs[PREF_USER_TYPE] = usertype
            launchActivity(SigninActivity::class.java)
        }

        loginAgrovet.setOnClickListener {

            showProgress("Login in...", "Accessing agrovet account")
            usertype = "Agrovet"
            val prefs = customPrefs(this)
            prefs[PREF_USER_TYPE] = usertype
            launchActivity(SigninActivity::class.java)
        }

//        loginAdmin.setOnClickListener {
//            showAlert(this,"accessing admin privileages !","")
//
//            showProgress("Login in...", "Accessing admin account")
//            usertype = "Admin"
//            val prefs = customPrefs(this)
//            prefs[PREF_USER_TYPE] = usertype
//            launchActivity(SigninActivity::class.java)
//        }

    }

    private fun launchActivity(intentClass: Class<*>) {
        dismissProgressDialog()
        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(com.marketgate.R.anim.fade_out, com.marketgate.R.anim.fade_in)
    }

    private fun showProgress(title: String = "", txt: String = "") {
        val b = Bundle()
        b.putString(DIA_TITLE, title)
        b.putString(DIA_TXT, txt)

        loader.setArguments(b)

        val tr = supportFragmentManager.beginTransaction()
        loader.show(tr, LoaderDialogue.TAG)
    }

    private fun dismissProgressDialog() {
        if (loader.isAdded && loader.isVisible)
            loader.dismiss()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        manageGoogleResult(requestCode, data)
    }

    override fun onGoogleCredentialReceived(credential: AuthCredential, account: GoogleSignInAccount) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    saveUser(user, account)

                    goToHome(usertype)
                } else {
                    // If sign in fails, display a message to the user.
                    dismissProgressDialog()
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(ce_emptynotif, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()

                }
            }
    }

    override fun onGoogleSignInFailed(e: ApiException) {
        dismissProgressDialog()
        Log.e(TAG, "signInWithCredential:failure", e)
        Snackbar.make(ce_emptynotif, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
    }


    private fun goToHome(usertype: String) {

        dismissProgressDialog()
        var intent: Intent? = null

        when (usertype) {
            "Farmer" -> {
                val prefs = customPrefs(this)
                prefs[PREF_USER_TYPE] = usertype
                intent = FarmerActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Agrovet" -> {
                val prefs = customPrefs(this)
                prefs[PREF_USER_TYPE] = usertype
                intent = AgentActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Agent" -> {
                val prefs = customPrefs(this)
                prefs[PREF_USER_TYPE] = usertype
                intent = AgentActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
            "Admin" -> {
                val prefs = customPrefs(this)
                prefs[PREF_USER_TYPE] = usertype
                intent = AdminActivity.newIntent(this).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
        }

        startActivity(intent)
        finish()
    }

    private fun saveUser(user: FirebaseUser?, account: GoogleSignInAccount) {
        when (usertype) {

            "Farmer" -> {
                firestore.collection(USER_FARMER).document(user!!.uid).get()
                    .addOnCompleteListener {
                        val result = it.result
                        if (!result!!.exists()) {

                            val n = getUserData(account)
                            val userFarmer = UserFarmer(
                                n.username, true, false, false, true, "Market Gate",
                                n.photoUrl, n.email, "", null, "Nairobi CBD", null, user.uid
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

                            val n = getUserData(account)
                            val userAgrovet = UserAgrovet(
                                n.username, true, false, false, true, "Market Gate Agrovet",
                                n.photoUrl, n.email, "", null, "Mombasa Near Hibernet", user.uid
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

                            val n = getUserData(account)
                            val userAgent = UserAgent(
                                n.username, true, false, false, true, "Market Gate Agent",
                                n.photoUrl, n.email, "", null, "Nyeri Farm Near Tomcat", user.uid
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

                            val n = getUserData(account)
                            val userAdmin = UserAdmin(
                                n.username, n.photoUrl, n.email, userid = user.uid
                            )

                            firestore.collection(USER_ADMIN).document(user.uid).set(userAdmin)
                        }
                    }
            }
        }
    }
}
