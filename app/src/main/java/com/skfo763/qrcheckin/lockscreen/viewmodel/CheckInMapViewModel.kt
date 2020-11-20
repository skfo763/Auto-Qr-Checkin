package com.skfo763.qrcheckin.lockscreen.viewmodel

import android.annotation.SuppressLint
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.skfo763.component.checkmap.NaverMapMarker
import com.skfo763.repository.checkinmap.CheckInMapException
import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CheckInMapViewModel @ViewModelInject constructor(
    private val checkInMapRepository: CheckInMapRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분")

    private val _cameraScopeAddress = MutableLiveData<CheckInAddress?>()
    private val _checkPointList = MutableLiveData<List<NaverMapMarker>>(listOf())

    val cameraScopeAddress: LiveData<CheckInAddress?> = _cameraScopeAddress
    val checkPointList: LiveData<List<NaverMapMarker>> = _checkPointList

    val onCameraPositionChanged: (LatLng) -> Unit = {
        viewModelScope.launch {
            getAddressFromLocation(it)
        }
    }

    private suspend fun getAddressFromLocation(latLng: LatLng) {
        withContext(Dispatchers.Main) {
            try {
                val address = checkInMapRepository.getAddressFromLocation(latLng.latitude, latLng.longitude)
                _cameraScopeAddress.value = address
                _checkPointList.value = checkInMapRepository.getCheckPoint(address).markerList
            } catch (e: CheckInMapException) {
                _cameraScopeAddress.value = null
            }
        }
    }

    fun dropLocationDatabase() {
        // TODO(데이터베이스 드랍시키는 로직 추가)
    }

    private val List<CheckPoint>.markerList: List<NaverMapMarker> get() = map {
        NaverMapMarker(
            it.checkPointIdx,
            it.latitude,
            it.longitude,
            "${it.address.largeSiDo} ${it.address.siGunGu} ${it.address.yupMyunDong}",
            dateFormat.format(Date(it.checkInTime))
        )
    }
}