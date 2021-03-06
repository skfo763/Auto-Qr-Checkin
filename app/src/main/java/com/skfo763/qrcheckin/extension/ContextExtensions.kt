package com.skfo763.qrcheckin.extension

import android.Manifest.permission
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skfo763.qrcheckin.R

fun Context.requestLocationPermissions(listener: PermissionListener) {
    TedPermission.with(this)
        .setPermissionListener(listener)
        .setDeniedTitle(getString(R.string.permission_denied_title))
        .setDeniedMessage(R.string.permission_denied_message)
        .setGotoSettingButtonText(R.string.permission_goto_settings)
        .setPermissions(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION)
        .check()
}

val Context.isOverlayPermissionGranted: Boolean get() = Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || Settings.canDrawOverlays(this)