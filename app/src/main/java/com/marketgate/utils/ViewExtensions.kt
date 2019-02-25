package com.raiachat.util

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.marketgate.R


inline fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT): Toast = Toast
    .makeText(this, message, duration)
    .apply {
        show()
    }

fun ImageView.loadUrl(url: Int) {
    Glide.with(context)
        .load(url)
        .thumbnail(0.05f)
        .into(this)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions().placeholder(R.drawable.avatar_placeholder))
        .thumbnail(0.05f)
        .into(this)
}

fun TextView.setFont(font: String) {
    this.typeface = Typeface.createFromAsset(context.assets, font)
}

fun TextView.setDrawable(icon: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
}

fun View.showView() {
    this.visibility = View.VISIBLE
}

fun View.hideView() {
    this.visibility = View.GONE
}

fun EditText.setDrawable(icon: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment).addToBackStack(fragment.tag) }
}

class SemiSquareLayout : RelativeLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec - 80)
    }

}

class SquareLayout : RelativeLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}

