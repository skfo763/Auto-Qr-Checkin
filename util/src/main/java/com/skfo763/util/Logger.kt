package com.skfo763.util

import java.lang.StringBuilder
import kotlin.reflect.KClass

object Logger {

    private const val ANSI_RESET = "\u001B[0m"
    private const val ANSI_BLACK = "\u001B[30m"
    private const val ANSI_RED = "\u001B[31m"
    private const val ANSI_GREEN = "\u001B[32m"
    private const val ANSI_YELLOW = "\u001B[33m"
    private const val ANSI_BLUE = "\u001B[34m"
    private const val ANSI_PURPLE = "\u001B[35m"
    private const val ANSI_CYAN = "\u001B[36m"
    private const val ANSI_WHITE = "\u001B[37m"
    
    @JvmStatic
    fun error(location: KClass<*>, message: String) {
        val fullMessage = StringBuilder(location.simpleName ?: "anonymous object").append(": ").append(message)
        println("$ANSI_RED$fullMessage$ANSI_RESET")
    }

    @JvmStatic
    fun error(e: Exception) {
        e.printStackTrace()
    }

}