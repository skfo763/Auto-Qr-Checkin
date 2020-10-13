package com.skfo763.component.floatingwidget

import android.app.Notification
import android.app.Service
import android.os.Build
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi

interface FloatingWidgetClient {

    val mWindowManager: WindowManager

    val params: WindowManager.LayoutParams

    val vibrator: Vibrator

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForegroundNotification(service: Service): Notification

}