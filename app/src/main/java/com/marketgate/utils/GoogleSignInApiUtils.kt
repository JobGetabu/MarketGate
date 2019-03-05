package com.marketgate.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object GoogleSignInApiUtils {

    private const val GOOGLE_ACCOUNT_DEFAULT_IMAGE_SIZE = "s96-c"
    private const val GOOGLE_ACCOUNT_DESIRED_IMAGE_SIZE = "s600-c"

    private fun retrieveResizedGoogleAccountPicture(account: GoogleSignInAccount): String? =
        account.photoUrl?.toString()?.replace(GOOGLE_ACCOUNT_DEFAULT_IMAGE_SIZE, GOOGLE_ACCOUNT_DESIRED_IMAGE_SIZE, true)
}
