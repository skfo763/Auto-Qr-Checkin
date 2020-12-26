package com.skfo763.component.checkstatelayout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.skfo763.base.extension.logException
import com.skfo763.component.R

class CheckStateLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttr) {

    @ColorInt private var nonCheckedBackground: Int = 0
    @ColorInt private var checkedBackground: Int = 0

    var isChecked: Boolean? = false
        set(value) {
            this.setBackgroundColor(if(value == true) checkedBackground else nonCheckedBackground)
            field = value
        }

    init {
        getAttributes(attributeSet)
    }

    private fun getAttributes(attributeSet: AttributeSet?) {
        attributeSet ?: return
        var typedArray: TypedArray? = null
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CheckStateLayout)
            nonCheckedBackground = typedArray.getColor(R.styleable.CheckStateLayout_nonCheckedBackground, ContextCompat.getColor(context, R.color.default_color_ffffff_000000))
            checkedBackground = typedArray.getColor(R.styleable.CheckStateLayout_checkedBackground, ContextCompat.getColor(context, R.color.checkin_button_background_tint))
        } catch (e: Exception) {
            logException(e)
        } finally {
            typedArray?.recycle()
        }
    }
}