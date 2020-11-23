package com.skfo763.component.floatingwidget

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
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

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(WIDGET_PUSH_ID, client.getForegroundNotification(this))
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
        super.onDestroy()
        client.mWindowManager.removeView(floatingWidgetView)
    }

    private fun setListeners() {
        floatingWidgetView.setEventListener(client, this)
    }
}