package com.skfo763.component.checkinbutton

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.skfo763.base.extension.dp
import io.reactivex.rxjava3.disposables.CompositeDisposable
import com.skfo763.component.databinding.LayoutCheckinButtonBinding

class CheckInButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    val binding: LayoutCheckinButtonBinding =
        LayoutCheckinButtonBinding.inflate(LayoutInflater.from(context), this, true)

    companion object {
        private const val ANIM_DURATION = 300L
    }

    val compositeDisposable = CompositeDisposable()

    var buttonVisibility: Boolean = this.isVisible
        set(value) {
            if(field == value) return
            if(value) showButton()
            else hideButton()
            field = value
        }

    private fun showButton(useAnimation: Boolean = true) {
        if(!useAnimation) { isVisible = true; return }
        animate().translationY((-12).dp(context))
            .setDuration(ANIM_DURATION)
            .setInterpolator(DecelerateInterpolator())
            .withStartAction {
                isVisible = true
            }.start()
    }

    private fun hideButton(useAnimation: Boolean = true) {
        if(!useAnimation) { isVisible = false; return }
        animate().translationY((12).dp(context))
            .setDuration(ANIM_DURATION)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                isVisible = false
            }.start()
    }
}