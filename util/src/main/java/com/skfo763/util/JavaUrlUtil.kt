package com.skfo763.util

import java.net.URL

val URL.queryMap: Map<String, String> get() {
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