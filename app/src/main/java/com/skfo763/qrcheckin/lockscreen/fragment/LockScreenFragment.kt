package com.skfo763.qrcheckin.lockscreen.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentLockScreenBinding
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.qrcheckin.lockscreen.viewmodel.LockScreenViewModel
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.lock_screen_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.toolbar_menu_developer_info -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.developer_info_title)
                    .setMessage(R.string.developer_info_message)
                    .setPositiveButton(R.string.confirm) { dialog, _ ->
                        dialog.dismiss()
                    }.show()
                super.onOptionsItemSelected(item)
            }
            R.id.toolbar_menu_logout -> {
                binding.lockScreenFragmentQrWebview.clearCacheData()
                parentViewModel.clearCaches(context?.externalCacheDir)
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }
}