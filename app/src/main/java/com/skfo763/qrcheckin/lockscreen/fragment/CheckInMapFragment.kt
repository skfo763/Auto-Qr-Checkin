package com.skfo763.qrcheckin.lockscreen.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.activityViewModels
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.skfo763.base.BaseFragment
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.databinding.FragmentCheckinMapBinding
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.qrcheckin.lockscreen.viewmodel.LockScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInMapFragment : BaseFragment<FragmentCheckinMapBinding, LockScreenViewModel, LockScreenActivityUseCase>(), OnMapReadyCallback {

    override val layoutResId: Int = R.layout.fragment_checkin_map

    override val parentViewModel: LockScreenViewModel by activityViewModels()

    override val useCase: LockScreenActivityUseCase by lazy { parentViewModel.useCase }

    override val bindingVariable: (FragmentCheckinMapBinding) -> Unit = {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.addObserver(binding.checkinMapView)
        binding.checkinMapView.onCreate(savedInstanceState)
        binding.checkinMapView.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.checkinMapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.checkinMapView.onLowMemory()
    }

    @UiThread
    override fun onMapReady(map: NaverMap) {
        Log.d("hellohello", map.toString())
    }

}