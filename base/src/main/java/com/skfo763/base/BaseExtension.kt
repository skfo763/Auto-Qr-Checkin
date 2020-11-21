package com.skfo763.base

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Any.logMessage(message: String?) {
    if(BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message ?: "unkwnown error")
    }
}

fun Any.logError(message: String?) {
    if(BuildConfig.DEBUG) {
        Log.e(this::class.java.simpleName, message ?: "unknown error")
    } else {
        FirebaseCrashlytics.getInstance().log(message ?: "unknown error")
    }
}

fun Any.logException(e: Exception) {
    if(BuildConfig.DEBUG) {
        e.printStackTrace()
    } else {
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}