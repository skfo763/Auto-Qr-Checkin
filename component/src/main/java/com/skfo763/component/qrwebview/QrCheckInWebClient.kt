package com.skfo763.component.qrwebview

import android.graphics.Bitmap
import android.util.Log
import android.webkit.*
import com.skfo763.base.logMessage
import com.skfo763.component.BuildConfig
import com.skfo763.component.extensions.parsedUri

class QrCheckInWebClient(
    private val webClientInterface: QrCheckInClientInterface
): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        logMessage("shouldOverrideUrlLoading : " + request?.url?.toString())
        return webClientInterface.onUrlLoadReceived(view, request?.url)
    }

    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return webClientInterface.onUrlLoadReceived(view, url.parsedUri)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        logMessage("onPageStarted : $url")
        return webClientInterface.onWebPageLoadStart(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        logMessage("onPageFinished : $url")
        super.onPageFinished(view, url)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        logMessage("onReceivedError" + errorResponse?.data?.toString())
        super.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        logMessage("onReceivedError" + error?.toString())
        super.onReceivedError(view, request, error)
    }

}