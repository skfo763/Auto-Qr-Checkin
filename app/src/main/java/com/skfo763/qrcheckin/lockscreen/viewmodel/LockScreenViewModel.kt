package com.skfo763.qrcheckin.lockscreen.viewmodel

import android.location.Location
import android.os.Bundle
import android.widget.CompoundButton
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.skfo763.base.BaseViewModel
import com.skfo763.base.BuildConfig
import com.skfo763.component.floatingwidget.FloatingWidgetService
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_X
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_Y
import com.skfo763.component.playcore.InAppReviewManager
import com.skfo763.component.qrwebview.ErrorFormat
import com.skfo763.component.tracker.FirebaseAnalyticsCustom
import com.skfo763.qrcheckin.lockscreen.service.LockScreenService
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.remote.data.QrCheckInError
import com.skfo763.repository.checkinmap.CheckInMapException
import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.lockscreen.LockScreenRepository
import com.skfo763.repository.model.CheckInAddress
import com.skfo763.repository.model.LanguageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class LockScreenViewModel @ViewModelInject constructor(
    private val lockScreenRepository: LockScreenRepository,
    private val checkInMapRepository: CheckInMapRepository,
    private val inAppReviewManager: InAppReviewManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<LockScreenActivityUseCase>() {

    private val random = Random()

    private val _isLockScreenChecked = MutableLiveData<Boolean>()
    private val _isWidgetChecked = MutableLiveData<Boolean>()
    private val _deleteAdsState = MutableLiveData<Boolean>()
    private val _availableHost = MutableLiveData<List<String>?>()
    private val _availablePath = MutableLiveData<List<String>?>()
    private val _appLandingScheme = MutableLiveData<List<String>?>()
    private val _errorList = MutableLiveData<List<ErrorFormat>?>()
    private val _urlForCheckIn = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _location = MutableLiveData<Location>()
    private val _versionName = MutableLiveData(BuildConfig.VERSION_NAME)

    val isLockScreenChecked: LiveData<Boolean> = _isLockScreenChecked
    val isWidgetChecked: LiveData<Boolean> = _isWidgetChecked
    val deleteAdsState: LiveData<Boolean> = _deleteAdsState
    val availableHost: LiveData<List<String>?> = _availableHost
    val availablePath: LiveData<List<String>?> = _availablePath
    val appLandingScheme: LiveData<List<String>?> = _appLandingScheme
    val errorList: LiveData<List<ErrorFormat>?> = _errorList
    val urlForCheckIn: LiveData<String?> = _urlForCheckIn
    val isLoading: LiveData<Boolean> = _isLoading
    val location: LiveData<Location> = _location
    val versionName: LiveData<String> = _versionName

    private val setLockScreenDataToCurrentSwitchState: (isChecked: Boolean) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setLockFeatureState(it)
        }
    }

    private val setWidgetDataToCurrentSwitchState: (isChecked: Boolean) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setWidgetFeatureState(it)
        }
    }

    private val setAdsDelete: (isDeleted: Boolean) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setDeleteAdsState(it)
        }
    }

    private val setLanguage: (LanguageState) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setLanguageState(it)
        }
    }

    private val setServiceStateWithCheckState: (isChecked: Boolean) -> Unit = {
        if(it) {
            useCase.startForegroundService(LockScreenService::class.java)
        } else {
            useCase.stopService(LockScreenService::class.java)
        }
    }

    val onFailedUrlLoaded: (invalidUrl: String?) -> Unit = {
        useCase.finishActivity()
    }

    val onOtherAppOpen: (openLink: String?) -> Unit  = { openLink ->
        useCase.startActivityForResult(openLink)
    }

    val setLockScreenSwitchToSavedState = {
        viewModelScope.launch {
            lockScreenRepository.getCurrentLockFeatureState().collect { isChecked ->
                _isLockScreenChecked.value = isChecked
            }
        }
    }

    val setWidgetSwitchToSavedState = {
        viewModelScope.launch {
            lockScreenRepository.getCurrentWidgetFeatureState().collect { isChecked ->
                _isWidgetChecked.value = isChecked
            }
        }
    }

    val onLockScreenSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        setLockScreenDataToCurrentSwitchState(isChecked)
        setServiceStateWithCheckState(isChecked)
    }

    val onWidgetSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        setWidgetDataToCurrentSwitchState(isChecked)
    }

    val onPushSwitchStateChanged: (CompoundButton, Boolean) -> Unit = { _, isChecked ->
        setWidgetDataToCurrentSwitchState(isChecked)
    }

    val onDeleteAdsClicked = {
        // TODO(광고 제거 버튼 클릭 로직 추가)
    }

    val onLanguageChangeClicked = {
        // TODO(언어 설정 변경 로직 추가)
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
        // TODO(버전 클릭 시 로직 추가)
    }

    fun deleteFloatingButton() {
        if(isWidgetChecked.value == true) {
            useCase.stopService(FloatingWidgetService::class.java)
        }
    }

    fun createFloatingButton() {
        if(isWidgetChecked.value == true) {
            val bundle = Bundle().apply {
                putInt(CURR_X, useCase.getIntentValue<Int>(CURR_X) ?: 200)
                putInt(CURR_Y, useCase.getIntentValue<Int>(CURR_Y) ?: 400)
            }
            useCase.startForegroundService(FloatingWidgetService::class.java, bundle)
        }
    }

    init {
        setLockScreenSwitchToSavedState()
        setWidgetSwitchToSavedState()
    }

    fun setQrCheckIn() {
        _isLoading.value = true
        viewModelScope.launch {
            lockScreenRepository.getNaverQrCheckInUrl().collect {
                _availableHost.value = it.availableHost
                _availablePath.value = it.availablePath
                _appLandingScheme.value = it.appLandingScheme
                _urlForCheckIn.value = it.url
                _errorList.value = convertToWebErrorFormat(it.errorList)
                _isLoading.value = false
            }
        }
    }

    private fun convertToWebErrorFormat(errorList: List<QrCheckInError>): List<ErrorFormat>? {
        return errorList.map { ErrorFormat(it.url, it.title, it.message, it.alternativeUrl) }
    }

    fun inAppReview() {
        if(random.nextInt(8) == 0) {
            inAppReviewManager.launchReviewFlow({
                if (it) {
                    sendReviewCompleteEvent(true)
                } else {
                    sendReviewCompleteEvent(false)
                }
            }) {
                FirebaseCrashlytics.getInstance().recordException(it)
            }
        }
    }

    private fun sendReviewCompleteEvent(complete: Boolean) {
        useCase.logAnalyticsEvent(
            FirebaseAnalyticsCustom.Event.IN_APP_REVIEW,
            Bundle().apply {
                putBoolean(FirebaseAnalyticsCustom.Param.REVIEW_COMPLETE, true)
            }
        )
    }

    fun sendCheckStateProperty() {
        useCase.sendUserProperty(
            "lock_screen_enabled",
            if (_isLockScreenChecked.value == true) "true" else "false"
        )
        useCase.sendUserProperty(
            "widget_enabled",
            if (_isWidgetChecked.value == true) "true" else "false"
        )
    }

    fun clearCaches(cacheDir: File?) {
        val appDir = File(cacheDir?.parent ?: return)
        if(appDir.exists()) {
            appDir.list()?.forEach {
                deleteDir(File(appDir, it))
            }
        }
        useCase.finishActivity()
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            dir.list()?.forEach {
                if (!deleteDir(File(dir, it))) return false
            }
        }
        return dir.delete()
    }

    fun requestLastKnownLocation() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                try {
                    _location.value = checkInMapRepository.getLastKnownLocation()
                } catch (e: CheckInMapException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun startTrackingLocation() {
        viewModelScope.launch {
            checkInMapRepository.startTrackingLocation()
        }
    }

    fun stopTrackingLocation() {
        viewModelScope.launch {
            checkInMapRepository.stopTrackingLocation()
        }
    }
}