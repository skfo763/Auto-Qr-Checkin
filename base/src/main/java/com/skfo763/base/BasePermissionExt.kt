package com.skfo763.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.crashlytics.FirebaseCrashlytics

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
        FirebaseCrashlytics.getInstance().log(e.message ?: "unknown")
    } catch (e: Exception) {
        Log.e(this::class.java.simpleName, e.message ?: "unknown")
    }
}