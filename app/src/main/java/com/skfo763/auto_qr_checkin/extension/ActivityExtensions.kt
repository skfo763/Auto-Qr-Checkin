package com.skfo763.auto_qr_checkin.extension

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize
import com.google.android.gms.ads.AdView
import com.skfo763.auto_qr_checkin.lockscreen.activity.LockScreenActivity.Companion.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE

fun AppCompatActivity.requestDismissKeyGuard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager)?.requestDismissKeyguard(this, null)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }
}

fun AppCompatActivity.clearDismissKeyGuard() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }
}

fun AppCompatActivity.registerScreenOnLocked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(true)
        setTurnScreenOn(true)
    } else {
        val complexFlags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        this.window.addFlags(complexFlags)
    }
}

fun AppCompatActivity.clearScreenOnLocked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setShowWhenLocked(false)
        setTurnScreenOn(false)
    } else {
        val complexFlags = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        window.clearFlags(complexFlags)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun AppCompatActivity.requestOverlayOptions() {
    val uri: Uri = Uri.fromParts("package", packageName, null)
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
    startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
}