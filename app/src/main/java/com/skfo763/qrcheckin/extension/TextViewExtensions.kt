package com.skfo763.qrcheckin.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.launch.LaunchIconManager
import com.skfo763.repository.model.CheckInAddress

@BindingAdapter("isDeleteAds")
fun TextView.setDeleteAdsTextView(isDeleteAds: Boolean) {
    if(isDeleteAds) {
        setText(R.string.nav_app_info_delete_ads_on)
    } else {
        setText(R.string.nav_app_info_delete_ads_off)
    }
}

@BindingAdapter("addressText")
fun TextView.setAddressText(address: CheckInAddress?) {
    address?.let {
        val addrString =  StringBuilder().apply {
            if(it.largeSiDo.isNotEmpty()) append(it.largeSiDo)
            if(it.siGunGu.isNotEmpty()) append(" ").append(it.siGunGu)
            if(it.yupMyunDong.isNotEmpty()) append(" ").append(it.yupMyunDong)
        }
        text = if(addrString.isEmpty()) context.getString(R.string.loading) else addrString
    } ?: run {
        context.getString(R.string.loading)
    }
}

@BindingAdapter("appIconImg")
fun ImageView.setAppIconImg(type: LaunchIconManager.Type?) {
    setImageDrawable(when(type ?: return) {
        LaunchIconManager.Type.LIGHT -> ContextCompat.getDrawable(context, R.drawable.launcher_icon_light)
        LaunchIconManager.Type.DARK -> ContextCompat.getDrawable(context, R.drawable.launcher_icon_dark)
    })
}

@BindingAdapter("appIconText")
fun TextView.setAppIconType(type: LaunchIconManager.Type?) {
    text = when(type ?: return) {
        LaunchIconManager.Type.LIGHT -> context.getString(R.string.icon_light_title)
        LaunchIconManager.Type.DARK -> context.getString(R.string.icon_dark_title)
    }
}