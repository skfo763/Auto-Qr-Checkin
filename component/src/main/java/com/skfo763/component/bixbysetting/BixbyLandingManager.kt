package com.skfo763.component.bixbysetting

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_LAUNCHER
import android.provider.Settings

class BixbyLandingManager(private val context: Context) {

    companion object {
        const val bixbyPackage = "com.samsung.android.bixby.agent"
        const val bixbyActivity = "com.samsung.android.bixby.settings.bixbykey.BixbyKeyActivity"
    }

    fun startBixbySettingIntent() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        context.startActivity(intent)
    }

}