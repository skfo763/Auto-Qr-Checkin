package com.skfo763.qrcheckin.extension

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase.Companion.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE

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

fun FragmentActivity.showOverlayPermissionDialog() {
    AlertDialog.Builder(this)
        .setTitle(R.string.permission_title)
        .setMessage(R.string.permission_message)
        .setCancelable(false)
        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestOverlayOptions()
            }
        }.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }.show()
}

@RequiresApi(Build.VERSION_CODES.M)
fun FragmentActivity.requestOverlayOptions() {
    val uri: Uri = Uri.fromParts("package", packageName, null)
    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
    startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
}