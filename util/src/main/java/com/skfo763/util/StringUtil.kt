package com.skfo763.util

import java.lang.Exception
import java.net.*

inline val String.parseUriString: URL? get() {
    return try {
        URL(this)
    } catch (e: Exception) {
        Logger.error(e)
        null
    }
}