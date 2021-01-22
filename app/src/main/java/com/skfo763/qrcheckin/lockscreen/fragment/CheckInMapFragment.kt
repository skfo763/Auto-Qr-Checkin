package com.skfo763.qrcheckin.lockscreen.fragment

import android.content.Intent
import android.content.res.Configuration
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.gun0912.tedpermission.PermissionListener
import com.naver.maps.geometry.LatLng
import com.skfo763.base.BaseFragment
import com.skfo763.base.theme.ThemeType
import com.skfo763.base.theme.getTheme
import com.skfo763.base.theme.isDarkTheme
import com.skfo763.component.checkmap.CheckInMapViewExt.shouldUseNaverMapOnly
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentCheckinMapBinding
import com.skfo763.qrcheckin.extension.requestLocationPermissions
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.LATITUDE
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.LONGITUDE
import com.skfo763.qrcheckin.lockscreen.service.FetchAddressIntentService.Companion.RECEIVER
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
        it.viewModel = viewModel
        it.setGotoCurrentButtonListener { context?.requestLocationPermissions(permissionListener) }
        it.checkinMapView.initializeMapSetting()
        it.checkinMapView.onCameraPositionChanged = this::searchAddress
    }

    private val permissionListener = object: PermissionListener {
        override fun onPermissionGranted() {
            viewModel.requestLastKnownLocation()
        }
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) = Unit
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(binding.checkinMapView)
        binding.checkinMapView.isNightModeEnabledState = isDarkTheme(requireContext().resources.configuration)
        binding.checkinMapView.onCreate(savedInstanceState)
        context?.requestLocationPermissions(permissionListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.checkin_map_toolbar_menu, menu)
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
                viewModel.deleteAllCheckPoint()
                useCase.finishActivity()
                super.onOptionsItemSelected(item)
            }
            else -> true
        }
    }

    private fun searchAddress(location: LatLng) {
        if(shouldUseNaverMapOnly(location.latitude, location.longitude) || !Geocoder.isPresent()) {
            viewModel.getNaverMapAddress(location)
        } else {
            activity ?: return
            val intent = Intent(activity, FetchAddressIntentService::class.java).apply {
                putExtra(LATITUDE, location.latitude)
                putExtra(LONGITUDE, location.longitude)
                putExtra(RECEIVER, viewModel.resultReceiver)
            }
            activity?.startService(intent)
        }
    }

}