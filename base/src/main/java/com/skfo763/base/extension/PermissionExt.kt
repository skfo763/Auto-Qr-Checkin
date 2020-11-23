package com.skfo763.base.extension

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

const val PERMISSION_SETTING_REQUEST_CODE = 27447351

fun Context.checkPermissionGranted(vararg permission: String): Boolean {
    return !permission.any {
        ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }
}

fun Fragment.checkNeverAskedPermission(vararg permission: String): Boolean {
    return permission.any {
        !shouldShowRequestPermissionRationale(it) && ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
    }
}

fun Fragment.startSettingActivity() {
    try {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivityForResult(intent, PERMISSION_SETTING_REQUEST_CODE)
    } catch (e: ActivityNotFoundException) {
        logError(e.message)
    } catch (e: Exception) {
        logException(e)
    }
}