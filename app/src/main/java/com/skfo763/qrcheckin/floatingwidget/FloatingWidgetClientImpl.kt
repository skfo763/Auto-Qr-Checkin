package com.skfo763.qrcheckin.floatingwidget

import android.app.Service
import android.os.Build
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.skfo763.component.floatingwidget.FloatingWidgetClient
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class FloatingWidgetClientImpl @Inject constructor(
    override val mWindowManager: WindowManager,
    override val params: WindowManager.LayoutParams,
    override val vibrator: Vibrator,
    private val notification: NotificationCompat.Builder
): FloatingWidgetClient {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getForegroundNotificationBuilder(service: Service) = notification

}