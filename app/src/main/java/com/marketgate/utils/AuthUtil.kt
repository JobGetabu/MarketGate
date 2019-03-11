package com.marketgate.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.marketgate.R
import com.marketgate.farmer.ProdDetailsActivity
import com.marketgate.farmer.ProdDetailsActivity.Companion.DOC_ID_EXTRA
import com.marketgate.models.USER_FARMER_Product
import com.tapadoo.alerter.Alerter


fun showDetails(context: Context, docId: String?){
    val intent = ProdDetailsActivity.newIntent(context).apply {
        putExtra(DOC_ID_EXTRA, docId)
    }
    context.startActivity(intent)
}

fun showPopup(view: View, context: Context, docId: String?) {
    var popup: PopupMenu? = null
    popup = PopupMenu(context, view)
    popup.inflate(R.menu.menu_list)

    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

        when (item!!.itemId) {
            R.id.menu_l_del -> {
                if (docId != null)
                FirebaseFirestore.getInstance().collection(USER_FARMER_Product).document(docId).delete()
                    .addOnSuccessListener { Toast.makeText(context,"Deleted !",Toast.LENGTH_SHORT).show() }
            }
            R.id.menu_l_view -> {
                val intent = ProdDetailsActivity.newIntent(context).apply {
                    putExtra(DOC_ID_EXTRA, docId)
                }
                context.startActivity(intent)
            }
        }
        true
    })
    popup.show()
}

fun showShortSnackbar(s: String, view: View) {
    Snackbar.make(
        view,
        s,
        Snackbar.LENGTH_LONG
    )
}

fun showAlert(activity: Activity, title: String, subtitle: String) {
    Alerter.create(activity)
        .setTitle(title)
        .setText(subtitle)
        .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
        .show()
}

fun showLongSnackbar(
    s: String,
    view: View,
    actionStringId: Int = com.marketgate.R.string.retry,
    listener: View.OnClickListener
) {
    Snackbar.make(
        view,
        s,
        Snackbar.LENGTH_INDEFINITE
    )
        .setAction(view.context.getString(actionStringId), listener).show()
}

fun UserAuthToastExceptions(@NonNull authtask: Task<AuthResult>, activity: Activity) {
    var error: String
    try {
        throw authtask.exception!!
    } catch (e: FirebaseAuthWeakPasswordException) {
        error = "Weak Password!"
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        error = "Invalid email"
    } catch (e: FirebaseAuthUserCollisionException) {
        error = "Existing Account"
    } catch (e: Exception) {
        error = "Unknown Error Occured"
        e.printStackTrace()
    }

    Toast.makeText(activity, error, Toast.LENGTH_LONG).show();

}


fun toMinutes(millisUntilFinished: Long): String {
    val min = millisUntilFinished / (1000 * 60)
    return min.toString()
}

fun toSec(millisUntilFinished: Long): String {
    val remainedSecs = millisUntilFinished / 1000
    return (remainedSecs % 60).toString()
}

fun fetchDrawable(@DrawableRes mdrawable: Int, ctx: Context): Drawable? {
    // Facade Design Pattern
    return ContextCompat.getDrawable(ctx, mdrawable)
}

fun fetchString(@StringRes mystring: Int, ctx: Context): String {
    // Facade Design Pattern
    return ctx.resources.getString(mystring)
}

fun fetchColor(@ColorRes color: Int, ctx: Context): Int {
    // Facade Design Pattern
    return ContextCompat.getColor(ctx, color)
}

