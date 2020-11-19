package com.skfo763.component.qrwebview

import android.net.Uri
import android.util.Log
import com.skfo763.component.extensions.containsAtLeast
import com.skfo763.component.extensions.getMatchedErrorFormat
import com.skfo763.component.extensions.hostSafeArg

class UriChecker {

    var doOnInvalidUrlLoaded: ((String?) -> Unit)? = null
    var doOnOpenOtherApp: ((String?) -> Unit)? = null
    var showDialog: ((Uri, ErrorFormat) -> Unit)? = null
    var loadUrl: ((String) -> Unit)? = null
    var availableHost: List<String> = listOf()
    var availablePath: List<String> = listOf()
    var appLandingScheme: List<String> = listOf()
    var errorList: List<ErrorFormat> = listOf()

    fun checkUrlFlow(uri: Uri?) {
        uri?.checkUriScheme()?.checkErrorFormat()?.checkAvailableHostAndPath()?.let {
            doOnInvalidUrlLoaded?.invoke(it.toString())
        } ?: run {

        }
    }

    private fun Uri.checkUriScheme(): Uri? {
        val safeScheme = scheme ?: return null
        return when {
            appLandingScheme.contains(safeScheme) -> {
                doOnOpenOtherApp?.invoke(this.toString())
                null
            }
            safeScheme == "http" -> {
                doOnInvalidUrlLoaded?.invoke(this.toString())
                null
            }
            else -> this
        }
    }

    private fun Uri.checkErrorFormat(): Uri? {
        errorList.getMatchedErrorFormat(this)?.let {
            showDialog?.invoke(this, it)
            return null
        }
        return this
    }

    private fun Uri.checkAvailableHostAndPath(): Uri? {
        return if(availableHost.contains(this.hostSafeArg) && availablePath.containsAtLeast(this.pathSegments)) {
            loadUrl?.invoke(this.toString())
            null
        } else this
    }

}