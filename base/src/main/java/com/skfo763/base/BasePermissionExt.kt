package com.skfo763.base

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Context.checkPermissionGranted(vararg permission: String): Boolean {
    return permission.firstOrNull {
        ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    } == null
}