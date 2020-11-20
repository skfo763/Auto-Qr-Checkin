package com.skfo763.qrcheckin.extension

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.skfo763.qrcheckin.R
import com.skfo763.repository.model.CheckInAddress

@BindingAdapter("isDeleteAds")
fun TextView.setDeleteAdsTextView(isDeleteAds: Boolean) {
    if(isDeleteAds) {
        setText(R.string.nav_app_info_delete_ads_on)
    } else {
        setText(R.string.nav_app_info_delete_ads_off)
    }
}