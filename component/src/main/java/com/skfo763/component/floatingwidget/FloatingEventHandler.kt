package com.skfo763.component.floatingwidget

import android.app.Service
import android.view.WindowManager

interface FloatingEventHandler {

    fun onWidgetClicked(client: FloatingWidgetClient, service: Service)

    fun onWidgetLongClicked(client: FloatingWidgetClient, service: Service)

    fun onWidgetMoved(client: FloatingWidgetClient, service: Service)

    fun updateViewLayout(client: FloatingWidgetClient)

}