package com.skfo763.component.qrwebview

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.skfo763.component.extensions.parsedUri
import com.skfo763.util.parseUriString

class QrCheckInWebClient(
    private val webClientInterface: QrCheckInClientInterface
): WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return webClientInterface.onUrlLoadReceived(view, request?.url)
    }

    @SuppressWarnings("deprecation")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return webClientInterface.onUrlLoadReceived(view, url.parsedUri)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        return webClientInterface.onWebPageLoadStart(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
    }

}