package com.skfo763.qrcheckin.floatingwidget

import android.app.Notification
import android.app.Service
import android.os.Build
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.skfo763.qrcheckin.R
import com.skfo763.component.floatingwidget.FloatingWidgetClient
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