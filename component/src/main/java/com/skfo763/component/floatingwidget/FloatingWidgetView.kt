package com.skfo763.component.floatingwidget

import android.app.Service
import android.content.Context
import android.util.AttributeSet
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.skfo763.component.R

abstract class FloatingWidgetView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), FloatingEventHandler {

    companion object {
        const val CURR_X = "widgetCurrentX"
        const val CURR_Y = "widgetCurrentY"
    }

    class ViewResId(val resId: Int = R.layout.layout_default_floating_widget)

    var currentX: Int = 0
    var currentY: Int = 0

    private var listener: FloatingWidgetEventListener? = null
        set(value) {
            setOnClickListener(value)
            setOnTouchListener(value)
            setOnLongClickListener(value)
            field = value
        }

    override fun updateViewLayout(client: FloatingWidgetClient) {
        client.mWindowManager.updateViewLayout(this, client.params)
    }

    override fun onWidgetClicked(client: FloatingWidgetClient, service: Service) {
        currentX = client.params.x
        currentY = client.params.y
    }

    fun setEventListener(client: FloatingWidgetClient, service: Service) {
        listener = FloatingWidgetEventListener(this, client, service)
    }

}

