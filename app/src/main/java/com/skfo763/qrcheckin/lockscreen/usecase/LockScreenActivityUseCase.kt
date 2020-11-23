package com.skfo763.qrcheckin.lockscreen.usecase

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.skfo763.base.BaseActivityUseCase
import com.skfo763.component.extensions.parsedUri
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.extension.requestOverlayOptions
import com.skfo763.qrcheckin.lockscreen.activity.LockScreenActivity
import com.skfo763.storage.gps.isLocationPermissionGranted

class LockScreenActivityUseCase constructor(
    private val activity: LockScreenActivity
) : BaseActivityUseCase(activity) {

    companion object {
        const val REQ_CODE_OPEN_OTHER_APP = 1000
        const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1001
    }

    var isActivityForeground: Boolean = false

    var onActivityInAppUpdateResult: ((Int, Intent?) -> Unit)? = null

    val isLocationPermissionGranted: Boolean get() = activity.isLocationPermissionGranted

    var snackBarWindow: View? = null

    val isOverlayPermissionDenied: Boolean get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)

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

    fun showSnackBar(message: String, action: (() -> Unit)? = null) {
        val snackBar = action?.let {
            Snackbar.make(snackBarWindow ?: activity.window.decorView, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.confirm) { it() }
        } ?: run {
            Snackbar.make(snackBarWindow ?: activity.window.decorView, message, Snackbar.LENGTH_LONG)
        }
        snackBar.show()
    }

    fun logAnalyticsEvent(event: String, param: Bundle) {
        activity.firebaseTracker.logAnalyticsEvent(event, param)
    }

    fun sendUserProperty(key: String, value: String) {
        activity.firebaseTracker.sendUserProperties(key, value)
    }

    @SuppressLint("NewApi")
    fun checkOverlayOptions() {
        if (isOverlayPermissionDenied) {
            showOverlayPermissionDialog {
                activity.requestOverlayOptions()
            }
            activity.viewModel.navigationViewModel.setWidgetDataToCurrentSwitchState(false)
        } else {
            activity.viewModel.navigationViewModel.setWidgetDataToCurrentSwitchState(true)
        }
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
            ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE -> {
                checkOverlayOptions()
                true
            }
            InAppUpdateManager.REQUEST_APP_UPDATE -> {
                onActivityInAppUpdateResult?.invoke(resultCode, data)
                true
            }
            else -> return super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showOverlayPermissionDialog(doOnPositiveClicked: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle(R.string.permission_title)
            .setMessage(R.string.permission_message)
            .setCancelable(false)
            .setPositiveButton(activity.getString(R.string.confirm)) { _, _ ->
                doOnPositiveClicked.invoke()
            }.setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun setActivityForegroundStateStart() {
        isActivityForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun setActivityForegroundStateStop() {
        isActivityForeground = false
    }

}