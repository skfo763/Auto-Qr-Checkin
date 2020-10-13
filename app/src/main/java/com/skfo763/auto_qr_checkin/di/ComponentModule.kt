package com.skfo763.auto_qr_checkin.di

import android.app.Service
import android.content.Context.VIBRATOR_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Build
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.skfo763.auto_qr_checkin.R
import com.skfo763.auto_qr_checkin.floatingwidget.FloatingWidgetClientImpl
import com.skfo763.component.floatingwidget.FloatingWidgetClient
import com.skfo763.component.floatingwidget.FloatingWidgetService
import com.skfo763.component.floatingwidget.FloatingWidgetView
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
abstract class ComponentModule {

    companion object {

        @Provides
        fun providesWindowManager(service: Service): WindowManager {
            return service.getSystemService(WINDOW_SERVICE) as WindowManager
        }

        @Provides
        fun providesVibrator(service: Service): Vibrator {
            return service.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        @Provides
        fun providesLayoutParams(): WindowManager.LayoutParams {
            return WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        @Provides
        fun providesLayoutResId(): FloatingWidgetView.ViewResId {
            return FloatingWidgetView.ViewResId(R.layout.widget_qr_floating_button)
        }
    }

    @Binds
    abstract fun bindFloatingWidgetClient(widgetClientImpl: FloatingWidgetClientImpl): FloatingWidgetClient

}