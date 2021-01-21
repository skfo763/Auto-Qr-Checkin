package com.skfo763.base.theme

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

fun getAvailableTheme() = listOf(
    ThemeType.LIGHT_MODE,
    ThemeType.DARK_MODE,
    ThemeType.DEFAULT_MODE
)

fun applyTheme(theme: ThemeType) {
    when(theme) {
        ThemeType.LIGHT_MODE -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        ThemeType.DARK_MODE -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        ThemeType.DEFAULT_MODE -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }
    }
}

fun getTheme(configuration: Configuration): ThemeType {
    return when(configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> ThemeType.LIGHT_MODE
        Configuration.UI_MODE_NIGHT_YES -> ThemeType.DARK_MODE
        else -> ThemeType.DEFAULT_MODE
    }
}

fun isDarkTheme(configuration: Configuration): Boolean {
    return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}