package com.skfo763.base

import android.util.Log

const val DEBUG_MESSAGE = "unknown debug message"

fun Any.logDebug(message: String?) {
    if(BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message ?: DEBUG_MESSAGE)
    }
}