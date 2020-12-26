package com.skfo763.qrcheckin.intro.usecase

import androidx.appcompat.app.AlertDialog
import com.skfo763.base.BaseActivityUseCase
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.extension.isOverlayPermissionGranted
import com.skfo763.qrcheckin.intro.activity.IntroActivity
import com.skfo763.storage.gps.isLocationPermissionGranted

class IntroActivityUseCase(private val activity : IntroActivity) : BaseActivityUseCase(activity) {

    fun goToPreviousInitFlow() {
        activity.goToPrevFlow()
    }

    fun completePermissionFlow() {
        if(activity.isOverlayPermissionGranted && activity.isLocationPermissionGranted) {
            activity.goToNextFlow()
        } else {
            AlertDialog.Builder(activity)
                .setTitle(R.string.intro_permission_dialog_title)
                .setMessage(R.string.intro_permisison_dialog_message)
                .setPositiveButton(activity.getString(R.string.confirm)) { _, _ ->
                    activity.viewModel.completeInitializeFlow()
                }.setNegativeButton(activity.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    fun completeOtherSettingFlow() {
        activity.goToNextFlow()
    }

    fun completeQrCheckInSettingFlow() {
        activity.goToNextFlow()
    }


}