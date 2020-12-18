package com.skfo763.qrcheckin.lockscreen.viewmodel

import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skfo763.base.extension.logException
import com.skfo763.component.bottomsheetdialog.AppIconSelectDialog
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.qrcheckin.BuildConfig
import com.skfo763.qrcheckin.launch.LaunchIconManager
import com.skfo763.qrcheckin.lockscreen.service.LockScreenService
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.repository.lockscreen.LockScreenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class NavigationViewModel(
    private val viewModelScope: CoroutineScope,
    private val lockScreenRepository: LockScreenRepository,
    private val inAppUpdateManager: InAppUpdateManager,
    private val random: Random
) {

    lateinit var useCase: LockScreenActivityUseCase

    private val _isLockScreenChecked = MutableLiveData<Boolean>()
    private val _isWidgetChecked = MutableLiveData<Boolean>()
    private val _isAutoCheckinChecked = MutableLiveData<Boolean>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _versionName = MutableLiveData(BuildConfig.VERSION_NAME)
    private val _appIconResource = MutableLiveData<LaunchIconManager.Type>()

    val isLockScreenChecked: LiveData<Boolean> = _isLockScreenChecked
    val isWidgetChecked: LiveData<Boolean> = _isWidgetChecked
    val isAutoCheckInChecked: LiveData<Boolean> = _isAutoCheckinChecked
    val isLoading: LiveData<Boolean> = _isLoading
    val versionName: LiveData<String> = _versionName
    val appIconResource: LiveData<LaunchIconManager.Type> = _appIconResource

    val onLockScreenSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        setLockScreenDataToCurrentSwitchState(isChecked)
        setServiceStateWithCheckState(isChecked)
    }

    val onWidgetSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        if(isChecked) {
            useCase.checkOverlayOptions()
        } else {
            setWidgetDataToCurrentSwitchState(isChecked)
        }
    }

    val onAutoCheckInSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        viewModelScope.launch {
            lockScreenRepository.setAutoCheckInState(isChecked)
        }
    }

    fun setLockScreenSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getCurrentLockFeatureState().collect {
                _isLockScreenChecked.value = it
            }
        }
    }

    fun setWidgetSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getCurrentWidgetFeatureState().collect {
                if(it) {
                    useCase.checkOverlayOptions()
                } else {
                    _isWidgetChecked.value = it
                }
            }
        }
    }

    fun setAutoCheckInSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getCurrentAutoCheckinState().collect {
                _isAutoCheckinChecked.value = it
            }
        }
    }

    fun setAppIconSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getAppIconState().collect {
                _appIconResource.value = LaunchIconManager.getType(it, useCase.currentUiTheme)
            }
        }
    }

    fun openAppResetInitializing() {
        viewModelScope.launch {
            lockScreenRepository.resetInitializationState(true)
            useCase.finishActivity()
        }
    }

    fun openAutoCheckInHelperView() {
        useCase.showAutoCheckinDescription()
    }

    fun startAppLauncherIconChangeFlow() {
        useCase.showAppIconSelectDialog()
    }

    fun setAppIconData(type: LaunchIconManager.Type) {
        viewModelScope.launch {
            lockScreenRepository.setAppIconType(type.manifestName)
        }
    }

    val setLockScreenDataToCurrentSwitchState: (isChecked: Boolean) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setLockFeatureState(it)
        }
    }

    val setWidgetDataToCurrentSwitchState: (Boolean) -> Unit = { isChecked ->
        _isWidgetChecked.value = isChecked
        viewModelScope.launch {
            lockScreenRepository.setWidgetFeatureState(isChecked)
        }
    }

    val setServiceStateWithCheckState: (Boolean) -> Unit = { isChecked ->
        if(isChecked) {
            useCase.startForegroundService(LockScreenService::class.java)
        } else {
            useCase.stopService(LockScreenService::class.java)
        }
    }

    val onReviewClicked = {
        _isLoading.value = true
        viewModelScope.launch {
            lockScreenRepository.getPlayStoreUrl().collect { url ->
                _isLoading.value = false
                useCase.openUrl(url)
            }
        }
    }

    val onShareClicked = {
        _isLoading.value = true
        viewModelScope.launch {
            lockScreenRepository.getPlayStoreUrl().collect {
                _isLoading.value = false
                useCase.sendUrl(it)
            }
        }
    }

    val onVersionClicked = {
        if(inAppUpdateManager.shouldUpdateApp(random)) {
            inAppUpdateManager.launchUpdateFlow {
                logException(it)
            }
        }
    }
}