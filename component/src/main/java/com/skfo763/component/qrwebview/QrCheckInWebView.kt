package com.skfo763.component.qrwebview

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import com.skfo763.component.extensions.containsAtLeast
import com.skfo763.component.extensions.hostSafeArg
import com.skfo763.component.extensions.queryMap

class QrCheckInWebView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attributeSet, defStyleAttr), QrCheckInClientInterface {

    private val webClient = QrCheckInWebClient(this)
    private val chromeClient = QrCheckInWebChromeClient()

    var doOnInvalidUrlLoaded: ((String?) -> Unit)? = null
    var availableHost: List<String> = listOf()
    var availablePath: List<String> = listOf()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    fun initWebViewSetting() {
        settings.apply {
            javaScriptEnabled = true
            databaseEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            setSupportZoom(false)
            webViewClient = webClient
            webChromeClient = chromeClient
        }
    }

    override fun onUrlLoadReceived(view: WebView?, uri: Uri?): Boolean {
        if(uri == null || uri.scheme == "http") {
            Log.e(this::class.simpleName, "URL parsing exception - protocol is http, rejected with security issue")
        } else {
            Log.d(this::class.simpleName, "${uri.hostSafeArg}, ${uri.pathSegments}, ${uri.queryMap}")
            checkUrl(uri.hostSafeArg, uri.pathSegments, uri.queryMap) { isValid ->
                if(isValid) {
                    loadUrl(uri.toString())
                } else {
                    doOnInvalidUrlLoaded?.invoke(url.toString())
                }
            }
        }
        return true
    }

    override fun onWebPageLoadStart(view: WebView?, url: String?, favicon: Bitmap?) = Unit

    private fun checkUrl(host: String, path: List<String>, queryMap: Map<String, String>, doAfterCheck: (Boolean) -> Unit) {
        if(availableHost.contains(host)) {
            if(availablePath.containsAtLeast(path)) {
                doAfterCheck(true)
            } else {
                return
            }
        } else {
            doAfterCheck(false)
        }
    }
}