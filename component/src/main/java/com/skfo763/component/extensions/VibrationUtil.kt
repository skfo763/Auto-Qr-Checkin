package com.skfo763.component.extensions

import android.os.VibrationEffect
import android.os.Vibrator

fun Vibrator.vibrateOneShot(milli: Long) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        vibrate(VibrationEffect.createOneShot(milli, 30))
    } else {
        vibrate((milli * 0.8).toLong())
    }
}