package com.skfo763.auto_qr_checkin.floatingwidget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.skfo763.auto_qr_checkin.R
import com.skfo763.auto_qr_checkin.lockscreen.activity.LockScreenActivity
import com.skfo763.component.floatingwidget.FloatingWidgetClient
import com.skfo763.component.floatingwidget.FloatingWidgetView
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class FloatingWidgetClientImpl @Inject constructor(
    override val mWindowManager: WindowManager,
    override val params: WindowManager.LayoutParams,
    override val vibrator: Vibrator,
    val notification: Notification
): FloatingWidgetClient {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getForegroundNotification(service: Service) = notification

}