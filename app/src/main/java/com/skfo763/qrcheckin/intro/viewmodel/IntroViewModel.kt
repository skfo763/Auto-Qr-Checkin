package com.skfo763.qrcheckin.intro.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gun0912.tedpermission.PermissionListener
import com.skfo763.base.BaseViewModel
import com.skfo763.qrcheckin.intro.usecase.IntroActivityUseCase
import com.skfo763.qrcheckin.lockscreen.activity.LockScreenActivity
import com.skfo763.repository.intro.IntroRepository
import kotlinx.coroutines.launch

class IntroViewModel @ViewModelInject constructor(
    private val repository: IntroRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
): BaseViewModel<IntroActivityUseCase>() {

    private val _overlayPermissionGranted = MutableLiveData(false)
    private val _locationPermissionGranted = MutableLiveData(false)

    val overlayPermissionGranted: LiveData<Boolean> = _overlayPermissionGranted
    val locationPermissionGranted: LiveData<Boolean> = _locationPermissionGranted

    fun setOverlayPermissionState(isGranted: Boolean) {
        _overlayPermissionGranted.value = isGranted
    }

    fun setLocationPermissionState(isGranted: Boolean) {
        _locationPermissionGranted.value = isGranted
    }

    fun completeInitializeFlow() {
        viewModelScope.launch {
            repository.saveInitializeState(false)
            useCase.startActivity(LockScreenActivity::class.java)
            useCase.finishActivity()
        }
    }

}