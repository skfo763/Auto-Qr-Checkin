package com.skfo763.qrcheckin.lockscreen.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.naver.maps.geometry.LatLng
import com.skfo763.base.extension.logException
import com.skfo763.base.theme.ThemeType
import com.skfo763.base.theme.isDarkTheme
import com.skfo763.component.checkmap.NaverMapMarker
import com.skfo763.qrcheckin.lockscreen.receiver.AddressResultReceiver
import com.skfo763.repository.checkinmap.CheckInMapException
import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.CheckPoint
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CheckInMapViewModel @ViewModelInject constructor(
    private val checkInMapRepository: CheckInMapRepository,
    val resultReceiver: AddressResultReceiver,
    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(), AddressResultReceiver.Listener {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")

    private val _location = MutableLiveData<Location>()
    private val _cameraScopeAddress = MutableLiveData<CheckInAddress?>()
    private val _checkPointList = MutableLiveData<List<NaverMapMarker>>(listOf())
    private val _isDarkModeEnabled = MutableLiveData<Boolean>()

    val location: LiveData<Location> = _location
    val cameraScopeAddress: LiveData<CheckInAddress?> = _cameraScopeAddress
    val checkPointList: LiveData<List<NaverMapMarker>> = _checkPointList
    val isDarkModeEnabled: LiveData<Boolean> = _isDarkModeEnabled

    init {
        this.resultReceiver.listener = this
    }

    fun setDarkModeEnabledState(isDarkTheme: Boolean) {
        _isDarkModeEnabled.value = isDarkTheme
    }

    fun requestLastKnownLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _location.value = checkInMapRepository.getLastKnownLocation() ?: return@withContext
                } catch (e: CheckInMapException) {
                    logException(e)
                }
            }
        }
    }

    fun getNaverMapAddress(latLng: LatLng) {
        viewModelScope.launch {
            getAddressFromLocation(latLng)
        }
    }

    fun deleteAllCheckPoint() {
        viewModelScope.launch {
            checkInMapRepository.deleteAllCheckPoint()
        }
    }

    private suspend fun getAddressFromLocation(latLng: LatLng) {
        withContext(Dispatchers.Main) {
            try {
                val address = checkInMapRepository.getAddressFromLocation(latLng.latitude, latLng.longitude)
                if(_cameraScopeAddress.equals(address)) return@withContext
                _cameraScopeAddress.value = address
                _checkPointList.value = checkInMapRepository.getCheckPoint(address).markerList
            } catch (e: CheckInMapException) {
                _cameraScopeAddress.value = null
            }
        }
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

    override fun onReceiveSuccess(result: CheckInAddress) {
        viewModelScope.launch {
            _cameraScopeAddress.value = result
            _checkPointList.value = checkInMapRepository.getCheckPoint(result).markerList
        }
    }

    override fun onReceiveError(data: Pair<Int, String>) {
        when(data.first) {

        }
    }
}