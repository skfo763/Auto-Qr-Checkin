package com.skfo763.base.extension

import android.content.Context

fun Int.dp(context: Context): Float {
    val screenDensity = context.resources.displayMetrics.density
    return (this * screenDensity)
}