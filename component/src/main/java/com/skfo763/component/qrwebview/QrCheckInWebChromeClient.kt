package com.skfo763.component.qrwebview

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import com.skfo763.component.qrwebview.QrCheckInWebView

class QrCheckInWebChromeClient : WebChromeClient() {

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        Log.d("QrCheckInWebView", "onJsAlert - ${message ?: "null"}")
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        Log.d("QrCheckInWebView", "onJsConfirm - ${message ?: "null"}")
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d("QrCheckInWebView", "onConsoleMessage - ${consoleMessage ?: "null"}")
        return super.onConsoleMessage(consoleMessage)
    }

}