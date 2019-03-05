package com.marketgate.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

const val RC_SIGN_IN = 1001

interface GoogleLoginCallback {
    val googleApiClient: GoogleSignInOptions
    val googleSingInClient: GoogleSignInClient

    fun logInWithGoogle(activity: Activity) {
        activity.startActivityForResult(googleSingInClient.signInIntent, RC_SIGN_IN)
    }

    fun manageGoogleResult(requestCode: Int, data: Intent?) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                onGoogleCredentialReceived(credential, account)
            } catch (e: ApiException) {
                Log.e ( "Login","Google sign in failed with error ${e.message}",e )
                onGoogleSignInFailed(e)
            }

        }
    }

    fun onGoogleCredentialReceived(credential: AuthCredential, account: GoogleSignInAccount)

    fun onGoogleSignInFailed(e: ApiException)
}