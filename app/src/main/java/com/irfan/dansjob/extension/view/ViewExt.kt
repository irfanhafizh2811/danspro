package com.irfan.dansjob.extension.view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat.startPostponedEnterTransition

fun View.getString(@StringRes stringResId: Int): String =
    this.context.resources.getString(stringResId)

fun View.showKeyboard() {
    val imm: InputMethodManager by lazy { this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    val imm: InputMethodManager by lazy { this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.goneIf(condition: Boolean) {
    if (condition) this.visibility = View.GONE else this.visibility = View.VISIBLE
}

fun View.invisibleIf(condition: Boolean) {
    if (condition) {
        this.visibility = View.INVISIBLE
        this.isEnabled = false
    } else {
        this.visibility = View.VISIBLE
        this.isEnabled = true
    }
}

fun View.isViewVisible(): Boolean = this.visibility == View.VISIBLE

fun View.scheduleStartPostponedTransition(activity: Activity) {
    this.viewTreeObserver.addOnPreDrawListener(
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                this@scheduleStartPostponedTransition.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition(activity)
                return true
            }
        })
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.getDimension(@DimenRes dimenResId: Int): Float =
    this.context.resources.getDimension(dimenResId)