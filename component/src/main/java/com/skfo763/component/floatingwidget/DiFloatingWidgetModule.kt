package com.skfo763.component.floatingwidget

import android.app.Service
import android.view.LayoutInflater
import android.view.View
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
abstract class DiFloatingWidgetModule {

    companion object {
        @Provides
        fun providesFloatingWidget(service: Service, resId: FloatingWidgetView.ViewResId): FloatingWidgetView {
            val view = LayoutInflater.from(service.applicationContext).inflate(resId.resId, null, false)
            (view as? FloatingWidgetView)?.let {
                return it
            } ?: run {
                throw IllegalStateException("Injected view is not instance of FloatingWidgetView")
            }
        }
    }

}