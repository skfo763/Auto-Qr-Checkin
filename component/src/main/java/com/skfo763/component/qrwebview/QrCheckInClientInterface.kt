package com.skfo763.component.qrwebview

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import java.net.URL

interface QrCheckInClientInterface {

    fun onUrlLoadReceived(view: WebView?, url: Uri?): Boolean

    fun onWebPageLoadStart(view: WebView?, url: String?, favicon: Bitmap?)
}