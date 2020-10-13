package com.skfo763.component.extensions

import android.net.Uri

val String?.parsedUri: Uri? get() = try {
    this?.let { Uri.parse(this) }
} catch (e: Exception) {
    null
}

val Uri.queryMap: Map<String, String> get() {
    val map = mutableMapOf<String, String>()
    try {
        query?.split("&")?.forEach {
            val name = it.split("=").getOrNull(0) ?: ""
            val value = it.split("=").getOrNull(1) ?: ""
            map[name] = value;
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
         return map
    }
}

val Uri.hostSafeArg: String get() = host ?: ""