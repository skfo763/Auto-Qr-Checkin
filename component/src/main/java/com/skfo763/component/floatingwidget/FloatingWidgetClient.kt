package com.skfo763.component.floatingwidget

import android.app.Service
import android.os.Build
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

interface FloatingWidgetClient {

    val mWindowManager: WindowManager

    val params: WindowManager.LayoutParams

    val vibrator: Vibrator

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForegroundNotificationBuilder(service: Service): NotificationCompat.Builder

}