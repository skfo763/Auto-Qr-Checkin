package com.skfo763.qrcheckin.lockscreen.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckInMapViewModel @ViewModelInject constructor(
    private val checkInMapRepository: CheckInMapRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _cameraScopeAddress = MutableLiveData<CheckInAddress>()
    private val _addressList = MutableLiveData<List<CheckPoint>>(listOf())

    val cameraScopeAddress: LiveData<CheckInAddress> = _cameraScopeAddress
    val addressList: LiveData<List<CheckPoint>> = _addressList


    val onCameraPositionChanged: (LatLng) -> Unit = {
        viewModelScope.launch {
            getAddressFromLocation(it)
        }
    }

    private suspend fun getAddressFromLocation(latLng: LatLng) {
        _cameraScopeAddress.value = checkInMapRepository.getAddressFromLocation(latLng.latitude, latLng.longitude)
    }

    private suspend fun getCheckPoint(address: CheckInAddress) {
        _addressList.value = checkInMapRepository.getCheckPoint(address)
    }

    fun dropLocationDatabase() {
        // TODO(데이터베이스 드랍시키는 로직 추가)
    }

}