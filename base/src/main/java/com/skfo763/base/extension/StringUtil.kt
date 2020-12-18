package com.skfo763.base.extension

import android.graphics.Color

val String.safeColor: Int get() {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        Color.WHITE
    }
}