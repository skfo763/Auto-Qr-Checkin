package com.skfo763.component.permissionstateview

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.skfo763.base.extension.logException
import com.skfo763.base.extension.logMessage
import com.skfo763.component.R
import com.skfo763.component.databinding.LayoutPermissionStateViewBinding

class PermissionStateView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attributeSet, defStyleAttr) {

    data class Model(
        val title: String,
        val subTitle: String,
        val iconRes: Int
    )

    private val binding: LayoutPermissionStateViewBinding =
        LayoutPermissionStateViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        getAttributes(attributeSet)
    }

    private fun getAttributes(attributeSet: AttributeSet?) {
        attributeSet ?: return
        var typedArray: TypedArray? = null
        var model: Model? = null
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.PermissionStateView)
            val title = typedArray.getString(R.styleable.PermissionStateView_psv_title) ?: ""
            val subTitle = typedArray.getString(R.styleable.PermissionStateView_psv_subtitle) ?: ""
            val iconRes = typedArray.getResourceId(R.styleable.PermissionStateView_psv_icon, R.drawable.ic_baseline_location_on_24)
            model = Model(title, subTitle, iconRes)
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
            binding.permissionStateViewTitle.text = model.title
            binding.permissionStateViewSubtitle.text = model.subTitle
            binding.permissionStateViewIcon.setImageResource(model.iconRes)
        } catch (e: Resources.NotFoundException) {
            logException(e)
        } catch (e: Exception) {
            logMessage(e.message)
        }
    }

    fun setPermissionState(isGranted: Boolean) {
        if(isGranted) {
            binding.permissionStateImage.setImageResource(R.drawable.ic_baseline_check_24)
        } else {
            binding.permissionStateImage.setImageResource(R.drawable.ic_baseline_clear_24)
        }
    }
}