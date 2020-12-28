package com.skfo763.qrcheckin.lockscreen.usecase

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.skfo763.base.BaseActivityUseCase
import com.skfo763.base.theme.ThemeType
import com.skfo763.base.theme.getTheme
import com.skfo763.component.bottomsheetdialog.MultiSelectDialog
import com.skfo763.component.bottomsheetdialog.AutoCheckInDescDialog
import com.skfo763.component.extensions.parsedUri
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.extension.isOverlayPermissionGranted
import com.skfo763.qrcheckin.extension.showOverlayPermissionDialog
import com.skfo763.qrcheckin.launch.LaunchIconManager
import com.skfo763.qrcheckin.lockscreen.activity.LockScreenActivity
import com.skfo763.repository.model.CheckInType
import com.skfo763.repository.model.qrCheckInType
import com.skfo763.storage.gps.isLocationPermissionGranted

class LockScreenActivityUseCase constructor(
    private val activity: LockScreenActivity
) : BaseActivityUseCase(activity) {

    companion object {
        const val REQ_CODE_OPEN_OTHER_APP = 1000
        const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1001
        const val REQ_CODE_OPEN_KAKAO_CHECKIN = 1002
    }

    val currentUiTheme: ThemeType get() = getTheme(activity)

    var isActivityForeground: Boolean = false

    var onActivityInAppUpdateResult: ((Int, Intent?) -> Unit)? = null

    val isLocationPermissionGranted: Boolean get() = activity.isLocationPermissionGranted

    var snackBarWindow: View? = null

    private var startKakaoActivityException: Exception? = null

    private val onAppIconSettingItemClicked: (MultiSelectDialog.Icon) -> Unit = { icon ->
        val launchType = LaunchIconManager.getType(icon.type, currentUiTheme)
        if(launchType != activity.viewModel.navigationViewModel.appIconResource.value) {
            activity.launchIconManager.setIcon(launchType)
            activity.viewModel.navigationViewModel.setAppIconData(launchType)
        }
    }

    private val onQrCheckInSettingItemClicked: ((MultiSelectDialog.Icon) -> Unit) = { icon ->
        activity.viewModel.navigationViewModel.setQrCheckInType(icon.type.qrCheckInType)
    }

    private val appIconList by lazy {
        listOf(
            MultiSelectDialog.Icon(
                LaunchIconManager.Type.LIGHT.manifestName,
                getDrawable(R.drawable.launcher_icon_light),
                getColor(R.color.app_icon_select_dialog_light_background),
                getColor(R.color.app_icon_select_dialog_light_title),
                getColor(R.color.app_icon_select_dialog_light_description),
                activity.getString(R.string.icon_light_title),
                activity.getString(R.string.icon_light_desc),
                onItemClicked = onAppIconSettingItemClicked
            ),
            MultiSelectDialog.Icon(
                LaunchIconManager.Type.DARK.manifestName,
                getDrawable(R.drawable.launcher_icon_dark),
                getColor(R.color.app_icon_select_dialog_dark_background),
                getColor(R.color.app_icon_select_dialog_dark_title),
                getColor(R.color.app_icon_select_dialog_dark_description),
                activity.getString(R.string.icon_dark_title),
                activity.getString(R.string.icon_dark_desc),
                onItemClicked = onAppIconSettingItemClicked
            )
        )
    }

    private val qrCheckInList by lazy {
        listOf(
            MultiSelectDialog.Icon(
                CheckInType.NAVER.type,
                getDrawable(R.drawable.naver_ci),
                getColor(R.color.app_icon_select_dialog_light_background),
                getColor(R.color.app_icon_select_dialog_light_title),
                getColor(R.color.app_icon_select_dialog_light_description),
                activity.getString(R.string.intro_qr_checkin_setting_naver_title),
                activity.getString(R.string.intro_qr_checkin_setting_naver_subtitle),
                onItemClicked = onQrCheckInSettingItemClicked
            ),
            MultiSelectDialog.Icon(
                CheckInType.KAKAO.type,
                getDrawable(R.drawable.kakao_ci),
                getColor(R.color.app_icon_select_dialog_dark_background),
                getColor(R.color.app_icon_select_dialog_dark_title),
                getColor(R.color.app_icon_select_dialog_dark_description),
                activity.getString(R.string.intro_qr_checkin_setting_kakao_title),
                activity.getString(R.string.intro_qr_checkin_setting_kakao_subtitle),
                onItemClicked = onQrCheckInSettingItemClicked
            )
        )
    }

    fun openMarketUrl(url: String?) {
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

    fun openKakaoUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.parsedUri)
            activity.startActivityForResult(intent, REQ_CODE_OPEN_KAKAO_CHECKIN)
        } catch (e: Exception) {
            startKakaoActivityException = e
            activity.startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.kakao.talk")
            ))
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
        if (!activity.isOverlayPermissionGranted) {
            activity.showOverlayPermissionDialog()
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
            REQ_CODE_OPEN_KAKAO_CHECKIN -> {
                if(startKakaoActivityException != null) return true
                activity.viewModel.saveCheckPointNowSubject.onNext(CheckInType.KAKAO.type)
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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun setActivityForegroundStateStart() {
        isActivityForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun setActivityForegroundStateStop() {
        isActivityForeground = false
    }

    fun getDrawable(@DrawableRes iconResId: Int): Drawable {
        return ContextCompat.getDrawable(this.activity, iconResId) ?: ColorDrawable(Color.TRANSPARENT)
    }

    @ColorInt
    fun getColor(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(this.activity, colorResId)
    }

    fun showAppIconSelectDialog() {
        val title = activity.getString(R.string.dialog_app_icon_select_title)
        MultiSelectDialog().apply {
            setData(title, appIconList)
            show(this@LockScreenActivityUseCase.activity.supportFragmentManager, null)
        }
    }

    fun showQrCheckInSettingDialog() {
        val title = activity.getString(R.string.intro_qr_checkin_setting_title)
        MultiSelectDialog().apply {
            setData(title, qrCheckInList)
            show(this@LockScreenActivityUseCase.activity.supportFragmentManager, null)
        }
    }

    fun showAutoCheckinDescription() {
        AutoCheckInDescDialog().show(activity.supportFragmentManager, null)
    }

}