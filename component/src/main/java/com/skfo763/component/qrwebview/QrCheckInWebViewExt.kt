package com.skfo763.component.qrwebview

import androidx.databinding.BindingAdapter
import com.skfo763.component.qrwebview.QrCheckInWebView

@BindingAdapter("checkInUrl")
fun QrCheckInWebView.loadCheckInUrl(url: String?) {
    url?.let {
        uriChecker.checkInUrl = it
        loadUrl(url)
    }
}

@BindingAdapter("availableHost")
fun QrCheckInWebView.setAvailableHost(hostList: List<String>?) {
    hostList?.let {
        uriChecker.availableHost = it
    }
}

@BindingAdapter("availablePath")
fun QrCheckInWebView.setAvailablePath(path: List<String>?) {
    path?.let {
        uriChecker.availablePath = it
    }
}

@BindingAdapter("appLandingScheme")
fun QrCheckInWebView.setAppLandingScheme(schemeList: List<String>?) {
    schemeList?.let {
        uriChecker.appLandingScheme = it
    }
}

@BindingAdapter("onInvalidUrlLoaded")
fun QrCheckInWebView.onInvalidUrlLoaded(func: (invalidUrl: String?) -> Unit) {
    uriChecker.doOnInvalidUrlLoaded = func
}

@BindingAdapter("onOtherAppOpen")
fun QrCheckInWebView.onOtherAppOpen(func: (openLink: String?) -> Unit) {
    uriChecker.doOnOpenOtherApp = func
}

@BindingAdapter("errorCaseList")
fun QrCheckInWebView.setErrorCase(errorList: List<ErrorFormat>?) {
    errorList?.let {
        uriChecker.errorList = errorList
    }
}

@BindingAdapter("onCheckIn")
fun QrCheckInWebView.setDoOnCheckIn(func: (url: String?) -> Unit) {
    uriChecker.doCheckIn = func
}