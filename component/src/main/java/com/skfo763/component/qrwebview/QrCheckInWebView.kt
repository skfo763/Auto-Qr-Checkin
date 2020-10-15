package com.skfo763.component.qrwebview

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import com.skfo763.component.BuildConfig
import com.skfo763.component.R
import com.skfo763.component.extensions.containsAtLeast
import com.skfo763.component.extensions.getMatchedErrorFormat
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
    var doOnOpenOtherApp: ((String?) -> Unit)? = null

    var availableHost: List<String> = listOf()
    var availablePath: List<String> = listOf()
    var errorList: List<ErrorFormat> = listOf()

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

        if(BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true)
        }
    }

    override fun onUrlLoadReceived(view: WebView?, uri: Uri?): Boolean {
        if(uri == null || uri.scheme == "http") {
            Log.e(this::class.simpleName, "URL parsing exception - protocol is http, rejected with security issue")
        } else {
            Log.d(this::class.simpleName, "${uri.hostSafeArg}, ${uri.pathSegments}, ${uri.queryMap}")
            checkUrl(uri) { isValid ->
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

    private fun checkUrl(uri: Uri, doAfterCheck: (Boolean) -> Unit) {
        errorList.getMatchedErrorFormat(uri)?.let {
            showDialog(it.title, it.message, it.alternativeUrl, uri.toString())
        } ?: run {
            doAfterCheck(availableHost.contains(uri.hostSafeArg) && availablePath.containsAtLeast(uri.pathSegments))
        }
    }

    private fun showDialog(title: String, message: String, linkUrl: String?, existingUrl: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                linkUrl?.let {
                    doOnOpenOtherApp?.invoke(if(linkUrl.isNotBlank()) linkUrl else existingUrl)
                }
                dialog.dismiss()
            }.show()
    }

}