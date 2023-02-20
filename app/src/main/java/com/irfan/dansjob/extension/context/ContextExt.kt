package com.irfan.dansjob.extension.context

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun Context.getColorCompat(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

fun Context.getDrawableCompat(@DrawableRes drawableId: Int) =
    AppCompatResources.getDrawable(this, drawableId)

fun Context.getDrawableCompat(@DrawableRes drawableId: Int, @ColorRes colorRes: Int) =
    AppCompatResources.getDrawable(this, drawableId)?.apply {
        val drawable = DrawableCompat.wrap(this)
        DrawableCompat.setTint(drawable, getColorCompat(colorRes))
    }

fun Context.hideKeyboard(view: View) {
    val inputMethodManager: InputMethodManager by lazy {
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(view: View) {
    view.postDelayed({
        val imm: InputMethodManager by lazy {
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }, 100)
}

fun Context.showKeyboardForce(view: View) {
    val inputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInputFromWindow(
        view.windowToken,
        InputMethodManager.SHOW_FORCED,
        0
    )
}

@Throws(RuntimeException::class)
fun Context.readJsonAssetToString(fileName: String): String {
    val builder = StringBuilder()
    try {
        val inputStream = this.assets.open(fileName)
        val buffer = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

        var str = buffer.readLine()
        while (str != null) {
            builder.append(str)
            str = buffer.readLine()
        }

        buffer.close()
        inputStream.close()
        return builder.toString()
    } catch (e: IOException) {
        e.printStackTrace()
        throw RuntimeException(e)
    }
}

fun Context.convertDpToPx(dp: Int): Int {
    return Math.round(
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), this.resources.displayMetrics
        )
    )
}

fun Context.getCurrentActivity(): Activity? {
    var curCtx = this
    while (curCtx is ContextWrapper) {
        if (curCtx is Activity) {
            return curCtx
        }
        curCtx = curCtx.baseContext
    }
    return null
}

fun Context.getTypefaceCompat(@FontRes id: Int): Typeface? = ResourcesCompat.getFont(this, id)

fun Context.getWidthScreenSize(): Int {
    val displayMetrics = DisplayMetrics()
    val activity = (this as Activity)
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun Context.getHeightScreenSize(): Int {
    val displayMetrics = DisplayMetrics()
    val activity = (this as Activity)
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}