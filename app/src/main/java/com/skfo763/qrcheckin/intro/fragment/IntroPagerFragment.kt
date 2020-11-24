package com.skfo763.qrcheckin.intro.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import com.gun0912.tedpermission.PermissionListener
import com.skfo763.base.BaseFragment
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentIntroOtherSettingBinding
import com.skfo763.qrcheckin.databinding.FragmentIntroPermissionBinding
import com.skfo763.qrcheckin.extension.isOverlayPermissionGranted
import com.skfo763.qrcheckin.extension.requestLocationPermissions
import com.skfo763.qrcheckin.extension.showOverlayPermissionDialog
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase
import com.skfo763.qrcheckin.intro.viewmodel.IntroViewModel
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase.Companion.ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE

sealed class IntroPagerFragment<T: ViewDataBinding> : BaseFragment<T, IntroViewModel, IntroActivityUseCase>()

class PermissionFragment: IntroPagerFragment<FragmentIntroPermissionBinding>() {

    override val layoutResId: Int = R.layout.fragment_intro_permission

    override val parentViewModel: IntroViewModel by activityViewModels()

    override val useCase: IntroActivityUseCase by lazy { parentViewModel.useCase }

    private val requestOverlayPermission = View.OnClickListener {
        checkOverlayOptions(true)
    }

    private val requestLocationPermission = View.OnClickListener {
        context?.requestLocationPermissions(locationPermissionListener)
    }

    override val bindingVariable: (FragmentIntroPermissionBinding) -> Unit = {
        it.parentViewModel = parentViewModel
        it.requestOverlayPermission = requestOverlayPermission
        it.requestLocationPermission = requestLocationPermission
    }

    private val locationPermissionListener = object: PermissionListener {
        override fun onPermissionGranted() {
            parentViewModel.setLocationPermissionState(true)
        }
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            parentViewModel.setLocationPermissionState(false)
        }
    }

    override fun onStart() {
        super.onStart()
        checkOverlayOptions(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                useCase.goToPreviousInitFlow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE -> {
                checkOverlayOptions(false)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @SuppressLint("NewApi")
    fun checkOverlayOptions(showDialog: Boolean) {
        if (activity?.isOverlayPermissionGranted == false) {
            parentViewModel.setOverlayPermissionState(false)
            if(showDialog) activity?.showOverlayPermissionDialog()
        } else {
            parentViewModel.setOverlayPermissionState(true)
            context?.requestLocationPermissions(locationPermissionListener)
        }
    }
}

class OtherSettingsFragment : IntroPagerFragment<FragmentIntroOtherSettingBinding>() {

    override val layoutResId: Int = R.layout.fragment_intro_other_setting

    override val parentViewModel: IntroViewModel by activityViewModels()

    override val useCase: IntroActivityUseCase by lazy { parentViewModel.useCase }

    override val bindingVariable: (FragmentIntroOtherSettingBinding) -> Unit = {
        it.parentViewModel = parentViewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                useCase.goToPreviousInitFlow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
