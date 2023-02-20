package com.irfan.dansjob.extension.activty

import android.app.Activity
import android.app.ActivityOptions
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Pair
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

fun Activity.getContentView(): View = this.findViewById(android.R.id.content)

fun Activity.startActivityWithTransition(intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    } else {
        this.startActivity(intent)
    }
}

fun Activity.startActivityWithSharedTransition(
    intent: Intent,
    vararg sharedElements: Pair<View, String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this, *sharedElements).toBundle()
        )
    } else {
        this.startActivity(intent)
    }
}

fun Activity.startPickImageActivity(intent: Intent, requestCode: Int, title: String) {
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT
    startActivityForResult(Intent.createChooser(intent, title), requestCode)
}

fun Context.hasPermissions(permissions: Array<String>): Boolean {
    permissions.forEach {
        if (ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

fun Context.checkPermissionResults(grantResults: IntArray): Boolean {
    grantResults.forEach {
        if (it != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}

fun Activity.runTimePermissions(permissions: Array<String>, permissionCode: Int) {
    ActivityCompat.requestPermissions(this, permissions, permissionCode)
}

fun Activity.getStatusBarDimension(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Activity.getActionBarSizeDimension(): Int {
    val ta = theme.obtainStyledAttributes(
        intArrayOf(android.R.attr.actionBarSize)
    )
    return ta.getDimension(0, 0f).toInt()
}

fun Activity.statusBarTranslucentVisible(visible: Boolean) {
    when (visible) {
        false -> window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        else -> window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

fun Window.blockTouchScreen() {
    this.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Window.unblockTouchScreen() {
    this.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun Activity.appInstalledOrNot(uri: String): Boolean {
    val pm = packageManager
    try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        return true
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return false
}

fun Activity.launchApp(packageApp: String) {
    val intent = packageManager.getLaunchIntentForPackage(packageApp)
    intent?.let { startActivity(it) }
}

fun Activity.openPlaystore(packageApp: String) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageApp")
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageApp")
            )
        )
    }
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}