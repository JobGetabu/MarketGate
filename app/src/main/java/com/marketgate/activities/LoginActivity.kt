package com.marketgate.activities

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
import com.marketgate.R
import com.marketgate.utils.GoogleLoginCallback
import com.marketgate.utils.LoaderDialogue
import com.marketgate.utils.LoaderDialogue.DIA_TITLE
import com.marketgate.utils.LoaderDialogue.DIA_TXT
import com.marketgate.utils.PreferenceHelper.PREF_USER_TYPE
import com.marketgate.utils.RC_SIGN_IN
import com.marketgate.utils.PreferenceHelper.customPrefs
import com.marketgate.utils.PreferenceHelper.get
import com.marketgate.utils.PreferenceHelper.set
import kotlinx.android.synthetic.main.login_prompt.*


class LoginActivity : AppCompatActivity() , GoogleLoginCallback {

    private lateinit var loader: LoaderDialogue
    private  var TAG: String = "login"
    private lateinit var auth: FirebaseAuth

    override val googleApiClient: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    override val googleSingInClient: GoogleSignInClient by lazy { GoogleSignIn.getClient(this, googleApiClient) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.marketgate.R.layout.login_prompt)

        loader = LoaderDialogue()

        //firebase
        auth = FirebaseAuth.getInstance()


        loginFarmer.setOnClickListener {
            showProgress("Login in...","Accessing farmers account")
            logInWithGoogle(this)
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

    private fun dismissProgressDialog(){
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

                    //TODO: save to DB

                    goToHome()
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


    private fun goToHome() {
        val prefs = customPrefs(this)
        prefs[PREF_USER_TYPE] = "Farmer"

        dismissProgressDialog()
        val intent = FarmerActivity.newIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
