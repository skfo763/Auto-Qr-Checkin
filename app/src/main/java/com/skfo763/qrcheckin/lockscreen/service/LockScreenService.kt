package com.skfo763.qrcheckin.lockscreen.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import com.skfo763.qrcheckin.lockscreen.receiver.ScreenReceiver
import com.skfo763.component.floatingwidget.FloatingWidgetService.Companion.WIDGET_PUSH_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LockScreenService @Inject constructor(): Service() {

    private val receiver: ScreenReceiver by lazy { ScreenReceiver() }
    @Inject lateinit var notification: Notification

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(receiver, intentFilter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(WIDGET_PUSH_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.action?.let {
            val intentFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
            registerReceiver(receiver, intentFilter)
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

}