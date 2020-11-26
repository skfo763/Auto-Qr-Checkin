package com.skfo763.component.floatingwidget

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.skfo763.base.extension.logException
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_X
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_Y
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class FloatingWidgetService @Inject constructor(): Service() {

    companion object {
        const val WIDGET_PUSH_ID = 3080
    }

    @Inject lateinit var floatingWidgetView: FloatingWidgetView
    @Inject lateinit var client: FloatingWidgetClient
    @Inject lateinit var launchIntent: Intent

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(
                    WIDGET_PUSH_ID,
                    getProcessedNotification(client.getForegroundNotificationBuilder(this))
                )
            }
        } catch (e: Exception) {
            logException(e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        client.params.x = intent?.getIntExtra(CURR_X, 0) ?: 0
        client.params.y = intent?.getIntExtra(CURR_Y, 0) ?: 0
        try {
            client.mWindowManager.addView(floatingWidgetView, client.params)
        } catch (e: RuntimeException) {
            logException(e)
        } finally {
            setListeners()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        if(floatingWidgetView.isAttachedToWindow) {
            client.mWindowManager.removeView(floatingWidgetView)
        }
        super.onDestroy()
    }

    private fun setListeners() {
        floatingWidgetView.setEventListener(client, this)
    }

    private fun getProcessedNotification(builder: NotificationCompat.Builder): Notification {
        return builder.setContentIntent(getPendingIntent()).build()
    }

    private fun getPendingIntent(): PendingIntent {
        return PendingIntent.getActivity(applicationContext, WIDGET_PUSH_ID, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}