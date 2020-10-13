package com.skfo763.auto_qr_checkin.extension

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.skfo763.auto_qr_checkin.R

@BindingAdapter("isDeleteAds")
fun TextView.setDeleteAdsTextView(isDeleteAds: Boolean) {
    if(isDeleteAds) {
        setText(R.string.nav_app_info_delete_ads_on)
    } else {
        setText(R.string.nav_app_info_delete_ads_off)
    }
}