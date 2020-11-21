package com.skfo763.component.playcore

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.skfo763.component.BuildConfig
import com.skfo763.component.R
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class InAppUpdateManager @Inject constructor(private val activity: Activity) {
    companion object {
        const val REQUEST_APP_UPDATE = 32145064
    }

    val appUpdateManager = AppUpdateManagerFactory.create(activity)
    var listener: InstallStateUpdatedListener? = null

    inline fun registerUpdateProgressListener(crossinline onProgress: (InstallState) -> Unit) {
        listener = InstallStateUpdatedListener { onProgress(it) }
        appUpdateManager.registerListener(listener ?: return)
    }

    fun unregisterUpdateProgressListener() {
        listener?.let {
            appUpdateManager.unregisterListener(it)
        }
    }

    fun shouldUpdateApp(random: Random) = false

    fun launchUpdateFlow(onRequestFailed: (Exception) -> Unit) {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener {
                if(it.isUpdateAvailable) {
                    startUpdateFlow(it)
                }
            }.addOnFailureListener {
                onRequestFailed(it)
            }
    }

    fun launchResetCompleteFlow(onUpdateProgress: (Float) -> Unit) {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener {
                when(it.installStatus()) {
                    InstallStatus.DOWNLOADED -> popupSnackBarForCompleteUpdate()
                    InstallStatus.DOWNLOADING -> onUpdateProgress(it.downloadPercentile)
                    else -> {

                    }
                }
            }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            AppUpdateType.FLEXIBLE,
            activity,
            REQUEST_APP_UPDATE
        )
    }

    private inline val AppUpdateInfo.isUpdateAvailable: Boolean get() {
        return updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
    }

    val handleInAppUpdateResult: (Int, Intent?) -> Unit = { resultCode, data ->
        when(resultCode) {
            RESULT_OK -> {
                popupSnackBarForCompleteUpdate()
            }
            RESULT_CANCELED -> {
                Log.e(this::class.java.simpleName, "app update canceled")
            }
            ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                Log.e(this::class.java.simpleName, "app update failed")
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            activity.window.decorView,
            activity.getString(R.string.update_complete),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.restart_app) {
            appUpdateManager.completeUpdate()
        }.show()
    }

    private inline val AppUpdateInfo.downloadPercentile: Float get() {
        return (bytesDownloaded().toFloat() / totalBytesToDownload()) * 100
    }

}