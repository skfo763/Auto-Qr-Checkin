package com.skfo763.auto_qr_checkin.floatingwidget

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.skfo763.auto_qr_checkin.databinding.ViewFloatingButtonBinding
import com.skfo763.auto_qr_checkin.lockscreen.activity.LockScreenActivity
import com.skfo763.component.extensions.vibrateOneShot
import com.skfo763.component.floatingwidget.FloatingWidgetClient
import com.skfo763.component.floatingwidget.FloatingWidgetView
import dagger.hilt.android.scopes.ServiceScoped


@ServiceScoped
class QrFloatingWidgetView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingWidgetView(context, attributeSet, defStyleAttr) {

    private val binding = ViewFloatingButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    var isLoading: Boolean = false
     set(value) {
         binding.floatingWidgetProgess.isVisible = value
         binding.floatingWidgetOpen.isVisible = !value
         binding.floatingWidgetText.isVisible = !value
         field = value
     }

    init {
        isLoading = false
    }

    override fun onWidgetClicked(client: FloatingWidgetClient, service: Service) {
        super.onWidgetClicked(client, service)
        if(binding.floatingWidgetClose.isVisible) {
            service.stopSelf()
        } else {
            isLoading = true

            val intent = Intent(service.applicationContext, LockScreenActivity::class.java).apply {
                putExtra(CURR_X, currentX)
                putExtra(CURR_Y, currentY)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
            )
            try {
                pendingIntent.send()
            } catch (e: PendingIntent.CanceledException) {
                service.startActivity(intent)
            }
        }
    }

    override fun onWidgetLongClicked(client: FloatingWidgetClient, service: Service) {
        client.vibrator.vibrateOneShot(80L)
        binding.floatingWidgetClose.isVisible = !binding.floatingWidgetClose.isVisible
    }

    override fun onWidgetMoved(client: FloatingWidgetClient, service: Service) {

    }

}