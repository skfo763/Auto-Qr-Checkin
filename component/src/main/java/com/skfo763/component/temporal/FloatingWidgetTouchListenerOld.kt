package com.skfo763.component.temporal

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlin.math.sqrt

/*
class FloatingWidgetTouchListenerOld(
    private val params: WindowManager.LayoutParams,
    private val density: Float,
    private val updateLayout: () -> Unit,
    private val onClick: () -> Unit,
    private val onLongClick: () -> Unit
) : View.OnTouchListener {

    companion object {
        private const val MAX_LONG_CLICK_DURATION = 1200L
        private const val MIN_CLICK_DURATION = 100
        private const val MAX_CLICK_DISTANCE = 3
    }

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0
    private var initialTouchY = 0
    private var touchX = 0.0f
    private var touchY = 0.0f

    private var startClickTime = System.currentTimeMillis()
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(this::class.simpleName, "MotionEvent.ACTION_DOWN")
                startClickTime = System.currentTimeMillis()
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX.toInt()
                initialTouchY = event.rawY.toInt()
                touchX = event.x
                touchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d(this::class.simpleName, "MotionEvent.ACTION_MOVE")
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                updateLayout()
            }
            MotionEvent.ACTION_UP -> {
                if(distance(touchX, touchY, event.x, event.y) < MAX_CLICK_DISTANCE) {
                    when (System.currentTimeMillis() - startClickTime) {
                        in 30L..MIN_CLICK_DURATION -> {
                            Log.d(this::class.simpleName, "MotionEvent.ACTION_CLICK")
                            onClick()
                        }
                        in MIN_CLICK_DURATION+100L..MAX_LONG_CLICK_DURATION -> {
                            Log.d(this::class.simpleName, "MotionEvent.ACTION_LONG_CLICK")
                            onLongClick()
                        }
                    }
                } else {
                    Log.d(this::class.simpleName, "MotionEvent.ACTION_UP")
                }
            }
        }
        return true
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x1 - x2
        val dy = y1 - y2
        val distanceInPx = sqrt(dx * dx + dy * dy.toDouble()).toFloat()
        return pxToDp(distanceInPx).apply {
            Log.d("hellohello", "dp = $distanceInPx")
        }
    }

    private fun pxToDp(px: Float): Float {
        return px / density
    }
}
 */