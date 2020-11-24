package com.skfo763.component.bixbysetting

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.skfo763.component.databinding.LayoutNumberIconTextViewBinding
import com.skfo763.base.extension.logException
import com.skfo763.base.extension.logMessage
import com.skfo763.component.R

class NumberIconTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttr) {

    data class Model(
        val title: String,
        val subTitle: String,
        @DrawableRes val numberResId: Int
    )

    enum class ViewState {
        ON_AIR,
        PLAIN
    }

    private val binding: LayoutNumberIconTextViewBinding =
        LayoutNumberIconTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        getAttributes(attributeSet)
    }

    private fun getAttributes(attributeSet: AttributeSet?) {
        attributeSet ?: return
        var typedArray: TypedArray? = null
        var model: Model? = null
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.NumberIconTextView)
            val title = typedArray.getString(R.styleable.NumberIconTextView_num_icon_tv_title) ?: ""
            val subTitle = typedArray.getString(R.styleable.NumberIconTextView_num_icon_tv_subtitle) ?: ""
            val number = typedArray.getInt(R.styleable.NumberIconTextView_num_icon_tv_number, 1)
            model = Model(title, subTitle, number.getNumberIconResId())
        } catch (e: Exception) {
            logException(e)
        } finally {
            typedArray?.recycle()
            setData(model)
        }
    }

    private fun setData(model: Model?) {
        model ?: return
        try {
            binding.numberIconTextViewTitle.text = model.title
            binding.numberIconTextViewSubtitle.text = model.subTitle
            binding.numberIconTextViewIcon.setImageResource(model.numberResId)
        } catch (e: Resources.NotFoundException) {
            logException(e)
        } catch (e: Exception) {
            logMessage(e.message)
        }
    }

    fun setViewState(viewState: ViewState) {
        when(viewState) {
            ViewState.ON_AIR -> {
                binding.numberIconTextViewTitle.setTextColor(Color.parseColor("#FF9800"))
                binding.numberIconTextViewSubtitle.setTextColor(Color.parseColor("#CD9643"))
            }
            ViewState.PLAIN -> {
                binding.numberIconTextViewTitle.setTextColor(Color.parseColor("#323232"))
                binding.numberIconTextViewSubtitle.setTextColor(Color.parseColor("#919191"))
            }
        }
    }

    @DrawableRes
    private fun Int.getNumberIconResId(): Int {
        return when(this) {
            1 -> R.drawable.ic_baseline_looks_one_24
            2 -> R.drawable.ic_baseline_looks_two_24
            3 -> R.drawable.ic_baseline_looks_3_24
            4 -> R.drawable.ic_baseline_looks_4_24
            5 -> R.drawable.ic_baseline_looks_5_24
            6 -> R.drawable.ic_baseline_looks_6_24
            else -> R.drawable.ic_baseline_exposure_zero_24
        }
    }
}