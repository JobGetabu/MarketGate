package com.marketgate.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object GoogleSignInApiUtils {

    private const val GOOGLE_ACCOUNT_DEFAULT_IMAGE_SIZE = "s96-c"
    private const val GOOGLE_ACCOUNT_DESIRED_IMAGE_SIZE = "s600-c"

    fun getUserData(account: GoogleSignInAccount): GUser {
        return GUser(email = account.email!!,
            photoUrl = retrieveResizedGoogleAccountPicture(account),
            username = "${account.displayName}",
            uid = "")
    }

    private fun retrieveResizedGoogleAccountPicture(account: GoogleSignInAccount): String? =
        account.photoUrl?.toString()?.replace(GOOGLE_ACCOUNT_DEFAULT_IMAGE_SIZE, GOOGLE_ACCOUNT_DESIRED_IMAGE_SIZE, true)
}

data class GUser(val email: String, val photoUrl: String?, val username: String, val uid: String )
