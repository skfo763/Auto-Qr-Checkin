package com.skfo763.component.checkinbutton

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skfo763.base.extension.dp

class CheckInButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attributeSet, defStyleAttr) {

    companion object {
        private const val ANIM_DURATION = 300L
    }

    var buttonVisibility: Boolean = this.isVisible
        set(value) {
            if(field == value) return
            if(value) showButton()
            else hideButton()
            field = value
        }


    fun showButton(useAnimation: Boolean = true) {
        if(!useAnimation) { isVisible = true; return }
        animate().translationY((-28).dp(context))
            .setDuration(ANIM_DURATION)
            .setInterpolator(DecelerateInterpolator())
            .withStartAction {
                isVisible = true
            }.start()
    }

    fun hideButton(useAnimation: Boolean = true) {
        if(!useAnimation) { isVisible = false; return }
        animate().translationY((28).dp(context))
            .setDuration(ANIM_DURATION)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                isVisible = false
            }.start()
    }

}