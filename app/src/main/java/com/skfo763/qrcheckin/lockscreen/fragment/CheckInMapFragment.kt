package com.skfo763.qrcheckin.lockscreen.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.skfo763.base.BaseFragment
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentCheckinMapBinding
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.qrcheckin.lockscreen.viewmodel.CheckInMapViewModel
import com.skfo763.qrcheckin.lockscreen.viewmodel.LockScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInMapFragment : BaseFragment<FragmentCheckinMapBinding, LockScreenViewModel, LockScreenActivityUseCase>() {

    override val layoutResId: Int = R.layout.fragment_checkin_map

    override val parentViewModel: LockScreenViewModel by activityViewModels()

    override val useCase: LockScreenActivityUseCase by lazy { parentViewModel.useCase }

    private val viewModel: CheckInMapViewModel by viewModels()

    override val bindingVariable: (FragmentCheckinMapBinding) -> Unit = {
        it.parentViewModel = this.parentViewModel
    }

    private val permissionListener = object: PermissionListener {
        override fun onPermissionGranted() {
            parentViewModel.requestLastKnownLocation()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(binding.checkinMapView)
        binding.checkinMapView.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.checkin_map_toolbar_menu, menu)
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.checkinMapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.checkinMapView.onLowMemory()
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
            R.id.toolbar_menu_drop_location_data -> {
                viewModel.dropLocationDatabase()
                useCase.finishActivity()
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }

    private fun requestPermission() {
        TedPermission.with(context ?: return)
            .setPermissionListener(permissionListener)
            .setDeniedTitle("권한거부")
            .setDeniedMessage("권한거부")
            .setGotoSettingButtonText("hellohello")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()
    }
}