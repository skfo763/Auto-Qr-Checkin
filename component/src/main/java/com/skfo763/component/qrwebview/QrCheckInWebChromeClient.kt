package com.skfo763.component.qrwebview

import android.webkit.ConsoleMessage
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.skfo763.base.logMessage

class QrCheckInWebChromeClient : WebChromeClient() {

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        logMessage("onJsAlert - ${message ?: "null"}")
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        logMessage("onJsConfirm - ${message ?: "null"}")
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        logMessage("onConsoleMessage - ${consoleMessage ?: "null"}")
        return super.onConsoleMessage(consoleMessage)
    }

}