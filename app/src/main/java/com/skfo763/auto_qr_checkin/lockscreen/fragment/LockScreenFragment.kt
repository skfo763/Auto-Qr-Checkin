package com.skfo763.auto_qr_checkin.lockscreen.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.skfo763.auto_qr_checkin.R
import com.skfo763.auto_qr_checkin.databinding.FragmentLockScreenBinding
import com.skfo763.auto_qr_checkin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.auto_qr_checkin.lockscreen.viewmodel.LockScreenViewModel
import com.skfo763.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LockScreenFragment : BaseFragment<FragmentLockScreenBinding, LockScreenViewModel, LockScreenActivityUseCase>() {

    override val layoutResId: Int = R.layout.fragment_lock_screen

    override val parentViewModel: LockScreenViewModel by activityViewModels()

    override val useCase: LockScreenActivityUseCase by lazy { parentViewModel.useCase }

    override val bindingVariable: (FragmentLockScreenBinding) -> Unit = {
        binding.lockScreenFragmentQrWebview.initWebViewSetting()
        it.parentViewModel = parentViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.setQrCheckIn()
    }

}