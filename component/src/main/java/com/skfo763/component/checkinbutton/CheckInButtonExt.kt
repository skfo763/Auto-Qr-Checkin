package com.skfo763.component.checkinbutton

import androidx.databinding.BindingAdapter

@BindingAdapter("buttonVisibility")
fun CheckInButton.setButtonVisibility(isVisible: Boolean?) {
    isVisible?.let {
        buttonVisibility = it
    }
}