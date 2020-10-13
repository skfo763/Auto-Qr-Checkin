package com.skfo763.component.floatingwidget

import android.annotation.SuppressLint
import android.app.Service
import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isInvisible
import kotlin.math.pow
import kotlin.math.sqrt

open class FloatingWidgetEventListener(
    private val eventHandler: FloatingEventHandler,
    private val client: FloatingWidgetClient,
    private val service: Service
) : View.OnLongClickListener, View.OnTouchListener, View.OnClickListener {

    private var downTime = 0L
    private var upTime = Long.MAX_VALUE

    private val initialPos = IntPos(client.params.x, client.params.y)
    private val touchPos = FloatPos(0f, 0f)

    override fun onLongClick(v: View): Boolean {
        val xDist = client.params.x - initialPos.x
        val yDist = client.params.y - initialPos.y
        if(getDistance(xDist.toFloat(), yDist.toFloat()) <= dpToPx(4)) {
            eventHandler.onWidgetLongClicked(client, service)
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downTime = System.currentTimeMillis()
                initialPos.x = client.params.x
                initialPos.y = client.params.y
                touchPos.x = event.rawX
                touchPos.y = event.rawY
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                client.params.x = initialPos.x + (event.rawX - touchPos.x).toInt()
                client.params.y = initialPos.y + (event.rawY - touchPos.y).toInt()
                eventHandler.updateViewLayout(client)
                eventHandler.onWidgetMoved(client, service)
                return true
            }
            MotionEvent.ACTION_UP -> {
                upTime = System.currentTimeMillis()
                return false
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        if(upTime - downTime <= 100L) {
            eventHandler.onWidgetClicked(client, service)
        }
    }

    class IntPos(var x: Int, var y: Int)

    class FloatPos(var x: Float, var y: Float)

    private fun getDistance(x: Float, y: Float): Float {
        return sqrt(x.pow(2) + y.pow(2))
    }

    private fun dpToPx(dp: Int) = dp * service.resources.displayMetrics.density

}