package com.skfo763.base.theme

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

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

fun getTheme(context: Context): ThemeType {
    return when(context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> ThemeType.LIGHT_MODE
        Configuration.UI_MODE_NIGHT_YES -> ThemeType.DARK_MODE
        else -> ThemeType.LIGHT_MODE
    }
}