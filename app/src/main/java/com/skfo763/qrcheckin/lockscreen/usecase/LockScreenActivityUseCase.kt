package com.skfo763.qrcheckin.lockscreen.usecase

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.skfo763.base.BaseActivityUseCase
import com.skfo763.component.extensions.parsedUri
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.qrcheckin.lockscreen.activity.LockScreenActivity

class LockScreenActivityUseCase constructor(
    private val activity: LockScreenActivity
) : BaseActivityUseCase(activity) {

    companion object {
        const val REQ_CODE_OPEN_OTHER_APP = 1000
    }

    var onActivityInAppUpdateResult: ((Int, Intent?) -> Unit)? = null

    fun openUrl(url: String?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url?.parsedUri ?: run {
                Uri.parse("market://details?id=${activity.packageName}")
            })
            activity.startActivity(intent)
        } catch (e: Exception) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}")
                )
            )
        }
    }

    fun sendUrl(url: String?) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra(Intent.EXTRA_TEXT, url ?: "https://play.google.com/store/apps/details?id=${activity.packageName}")
                putExtra(Intent.EXTRA_TITLE, "앱 공유하기")
                type = "text/plain"
            }
            logAnalyticsEvent(FirebaseAnalytics.Event.SHARE, bundleOf(FirebaseAnalytics.Param.METHOD to "url"))
            activity.startActivity(Intent.createChooser(intent, "앱을 선택해 주세요"))
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    fun showToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    fun logAnalyticsEvent(event: String, param: Bundle) {
        activity.firebaseTracker.logAnalyticsEvent(event, param)
    }

    fun sendUserProperty(key: String, value: String) {
        activity.firebaseTracker.sendUserProperties(key, value)
    }

    fun startActivityForResult(openLink: String?) {
        openLink?.let {
            try {
                activity.startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse(it)), REQ_CODE_OPEN_OTHER_APP)
            } catch (e: Exception) {
                activity.startActivity(Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.nhn.android.search")
                ))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return when(requestCode) {
            REQ_CODE_OPEN_OTHER_APP -> {
                activity.viewModel.setQrCheckIn()
                true
            }
            InAppUpdateManager.REQUEST_APP_UPDATE -> {
                onActivityInAppUpdateResult?.invoke(resultCode, data)
                true
            }
            else -> return super.onActivityResult(requestCode, resultCode, data)
        }
    }


}