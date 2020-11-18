package com.skfo763.component.qrwebview

import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrCheckInWebView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attributeSet, defStyleAttr), QrCheckInClientInterface {

    private val webClient = QrCheckInWebClient(this)
    private val chromeClient = QrCheckInWebChromeClient()
    val uriChecker = UriChecker()

    init {
        isFocusable = true
        isFocusableInTouchMode = true

        uriChecker.showDialog = { uri, it -> showDialog(it.title, it.message, it.alternativeUrl, uri.toString()) }
        uriChecker.loadUrl = { this.loadUrl(it) }
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

    fun clearCacheData() {
        clearCache(true)
        clearHistory()
        clearFormData()
    }

    override fun onUrlLoadReceived(view: WebView?, uri: Uri?): Boolean {
        uriChecker.checkUrlFlow(uri)
        return true
    }

    override fun onWebPageLoadStart(view: WebView?, url: String?, favicon: Bitmap?) = Unit

    private fun showDialog(title: String, message: String, linkUrl: String?, existingUrl: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                linkUrl?.let {
                    uriChecker.doOnOpenOtherApp?.invoke(if(linkUrl.isNotBlank()) linkUrl else existingUrl)
                }
                dialog.dismiss()
            }.show()
    }

}