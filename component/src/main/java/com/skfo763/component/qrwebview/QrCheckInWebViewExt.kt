package com.skfo763.component.qrwebview

import androidx.databinding.BindingAdapter
import com.skfo763.component.qrwebview.QrCheckInWebView

@BindingAdapter("checkInUrl")
fun QrCheckInWebView.loadCheckInUrl(url: String?) {
    url?.let {
        loadUrl(url)
    }
}

@BindingAdapter("availableHost")
fun QrCheckInWebView.setAvailableHost(hostList: List<String>?) {
    hostList?.let {
        availableHost = it
    }
}

@BindingAdapter("availablePath")
fun QrCheckInWebView.setAvailablePath(path: List<String>?) {
    path?.let {
        availablePath = it
    }
}

@BindingAdapter("onInvalidUrlLoaded")
fun QrCheckInWebView.onInvalidUrlLoaded(func: (invalidUrl: String?) -> Unit) {
    this.doOnInvalidUrlLoaded = func
}

@BindingAdapter("onOtherAppOpen")
fun QrCheckInWebView.onOtherAppOpen(func: (openLink: String?) -> Unit) {
    this.doOnOpenOtherApp = func
}

@BindingAdapter("errorCaseList")
fun QrCheckInWebView.setErrorCase(errorList: List<ErrorFormat>?) {
    errorList?.let {
        this.errorList = errorList
    }
}