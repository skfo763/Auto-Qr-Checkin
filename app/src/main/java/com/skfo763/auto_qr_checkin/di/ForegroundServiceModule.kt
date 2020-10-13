package com.skfo763.auto_qr_checkin.di

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.skfo763.auto_qr_checkin.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
abstract class ForegroundServiceModule {

    companion object {

        @Provides
        @RequiresApi(Build.VERSION_CODES.O)
        fun provideForegroundNotification(service: Service): Notification {
            val channelId = service.getString(R.string.noti_channel_id)
            val strTitle = service.getString(R.string.app_name_full)

            val notificationManager = service.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    strTitle,
                    NotificationManager.IMPORTANCE_NONE
                ).apply {
                    description = strTitle
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                }
                notificationManager.createNotificationChannel(notificationChannel)
            }

            return NotificationCompat.Builder(service.applicationContext, channelId).apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setDefaults(Notification.DEFAULT_ALL)
                setAutoCancel(false)
                setContentTitle(service.getString(R.string.app_name_full))
                setContentText(service.getString(R.string.service_notification_text))
            }.build()
        }
    }


}