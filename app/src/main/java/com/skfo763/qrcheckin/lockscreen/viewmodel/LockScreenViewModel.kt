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
import com.gun0912.tedpermission.PermissionListener
import com.skfo763.base.BaseViewModel
import com.skfo763.base.BuildConfig
import com.skfo763.base.logException
import com.skfo763.component.bixbysetting.BixbyLandingManager
import com.skfo763.component.floatingwidget.FloatingWidgetService
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_X
import com.skfo763.component.floatingwidget.FloatingWidgetView.Companion.CURR_Y
import com.skfo763.component.playcore.InAppReviewManager
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.component.qrwebview.ErrorFormat
import com.skfo763.component.tracker.FirebaseAnalyticsCustom
import com.skfo763.qrcheckin.R
import com.skfo763.qrcheckin.lockscreen.service.LockScreenService
import com.skfo763.qrcheckin.lockscreen.usecase.LockScreenActivityUseCase
import com.skfo763.remote.data.QrCheckInError
import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.lockscreen.LockScreenRepository
import com.skfo763.repository.model.CheckInUrl
import com.skfo763.repository.model.CheckPoint
import com.skfo763.storage.gps.GpsException
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import kotlin.math.sqrt

class LockScreenViewModel @ViewModelInject constructor(
    private val lockScreenRepository: LockScreenRepository,
    private val checkInMapRepository: CheckInMapRepository,
    private val inAppReviewManager: InAppReviewManager,
    private val inAppUpdateManager: InAppUpdateManager,
    private val bixbyLandingManager: BixbyLandingManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<LockScreenActivityUseCase>() {

    companion object {
        private const val minDistance = 4.242640687119285E-5
    }

    private val random = Random()
    private var currLocation = Pair(0.0, 0.0)
    private val subject: Subject<String> = BehaviorSubject.create()
    val shouldReviewApp: Boolean get() = inAppReviewManager.shouldReviewApp(random)

    private val _isLockScreenChecked = MutableLiveData<Boolean>()
    private val _isWidgetChecked = MutableLiveData<Boolean>()
    private val _availableHost = MutableLiveData<List<String>?>()
    private val _availablePath = MutableLiveData<List<String>?>()
    private val _appLandingScheme = MutableLiveData<List<String>?>()
    private val _errorList = MutableLiveData<List<ErrorFormat>?>()
    private val _urlForCheckIn = MutableLiveData<String?>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _versionName = MutableLiveData(BuildConfig.VERSION_NAME)

    val isLockScreenChecked: LiveData<Boolean> = _isLockScreenChecked
    val isWidgetChecked: LiveData<Boolean> = _isWidgetChecked
    val availableHost: LiveData<List<String>?> = _availableHost
    val availablePath: LiveData<List<String>?> = _availablePath
    val appLandingScheme: LiveData<List<String>?> = _appLandingScheme
    val errorList: LiveData<List<ErrorFormat>?> = _errorList
    val urlForCheckIn: LiveData<String?> = _urlForCheckIn
    val isLoading: LiveData<Boolean> = _isLoading
    val versionName: LiveData<String> = _versionName

    private val setLockScreenDataToCurrentSwitchState: (isChecked: Boolean) -> Unit = {
        viewModelScope.launch {
            lockScreenRepository.setLockFeatureState(it)
        }
    }

    val setWidgetDataToCurrentSwitchState: (isChecked: Boolean) -> Unit = {
        _isWidgetChecked.value = it
        viewModelScope.launch {
            lockScreenRepository.setWidgetFeatureState(it)
        }
    }

    private val setServiceStateWithCheckState: (isChecked: Boolean) -> Unit = {
        if(it) {
            useCase.startForegroundService(LockScreenService::class.java)
        } else {
            useCase.stopService(LockScreenService::class.java)
        }
    }

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
        inAppReviewManager.launchReviewFlow({
            sendReviewCompleteEvent(it)
        }) {
            logException(it)
        }
    }

    val onFailedUrlLoaded: (invalidUrl: String?) -> Unit = {
        useCase.finishActivity()
    }

    val onOtherAppOpen: (openLink: String?) -> Unit  = { openLink ->
        useCase.startActivityForResult(openLink)
    }

    val startTrackingLocationListener = object: PermissionListener {
        override fun onPermissionGranted() {
            viewModelScope.launch {
                startInAppUpdateFlow()
                checkInMapRepository.startTrackingLocation()
            }
        }
        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) = Unit
    }

    val stopTrackingLocation: () -> Unit = {
        viewModelScope.launch {
            checkInMapRepository.stopTrackingLocation()
        }
    }

    val onCheckInComplete: (String?) -> Unit = {
        subject.onNext(it ?: "")
    }

    init {
        observeCheckInFlowable()
    }

    private fun observeCheckInFlowable() {
        subject.toSerialized().toFlowable(BackpressureStrategy.BUFFER)
            .observeOn(Schedulers.io())
            .throttleFirst(2000L, TimeUnit.MILLISECONDS)
            .delay(5000L, TimeUnit.MILLISECONDS)
            .subscribe({
                if(it.isEmpty()) return@subscribe
                saveCheckPoint()
            }) { logException(Exception(it)) }
    }

    fun setSwitchToSavedState() {
        setLockScreenSavedState()
        setWidgetSavedState()
    }

    private fun setLockScreenSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getCurrentLockFeatureState().collect { isChecked ->
                _isLockScreenChecked.value = isChecked
            }
        }
    }

    private fun setWidgetSavedState() {
        viewModelScope.launch {
            lockScreenRepository.getCurrentWidgetFeatureState().collect { isChecked ->
                if(isChecked) {
                    useCase.checkOverlayOptions()
                } else {
                    _isWidgetChecked.value = isChecked
                }
            }
        }
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

    fun setQrCheckIn() {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                lockScreenRepository.getNaverQrCheckInUrl().collect {
                    setCheckInUrlInfo(it)
                }
            }
        }
    }

    private suspend fun setCheckInUrlInfo(checkInUrl: CheckInUrl) {
        withContext(Dispatchers.Main) {
            _availableHost.value = checkInUrl.availableHost
            _availablePath.value = checkInUrl.availablePath
            _appLandingScheme.value = checkInUrl.appLandingScheme
            _urlForCheckIn.value = checkInUrl.url
            _errorList.value = convertToWebErrorFormat(checkInUrl.errorList)
            _isLoading.value = false
        }
    }

    private fun convertToWebErrorFormat(errorList: List<QrCheckInError>): List<ErrorFormat>? {
        return errorList.map { ErrorFormat(it.url, it.title, it.message, it.alternativeUrl) }
    }

    fun startReviewFlow(onReviewFlowEnded: () -> Unit) {
        inAppReviewManager.launchReviewFlow({
            sendReviewCompleteEvent(it)
            onReviewFlowEnded()
        }) {
            logException(it)
            onReviewFlowEnded()
        }
    }

    fun startInAppUpdateFlow() {
        useCase.onActivityInAppUpdateResult = inAppUpdateManager.handleInAppUpdateResult
        if(inAppUpdateManager.shouldUpdateApp(random)) {
            inAppUpdateManager.launchUpdateFlow {
                logException(it)
            }
        }
    }

    private fun sendReviewCompleteEvent(complete: Boolean) {
        useCase.logAnalyticsEvent(
            FirebaseAnalyticsCustom.Event.IN_APP_REVIEW,
            Bundle().apply {
                putBoolean(FirebaseAnalyticsCustom.Param.REVIEW_COMPLETE, complete)
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

    private fun saveCheckPoint() {
        if(!useCase.isActivityForeground || !useCase.isLocationPermissionGranted) return
        viewModelScope.launch {
            try {
                val location = checkInMapRepository.getLastKnownLocation() ?: return@launch
                if(location.isSameCoordinate(currLocation)) return@launch
                currLocation = location.latitude to location.longitude
                val address = checkInMapRepository.getAddressFromLocation(location.latitude, location.longitude)
                val checkPoint = CheckPoint(location.latitude, location.longitude, address, System.currentTimeMillis())
                if(checkInMapRepository.saveCheckPoint(checkPoint)) {
                    showCheckInConfirmSnackBar(checkPoint)
                }
            } catch (e: GpsException) {
                logException(Exception(e))
            } catch (e: Exception) {
                logException(Exception(e))
            }
        }
    }

    private fun showCheckInConfirmSnackBar(checkIn: CheckPoint) {
        useCase.showSnackBar(
            useCase.res.getString(R.string.location_checkin_complete) +
                    "${checkIn.address.largeSiDo} ${checkIn.address.siGunGu} ${checkIn.address.yupMyunDong}")
    }

    private fun Location.isSameCoordinate(prevLocation: Pair<Double, Double>): Boolean {
        val latDistance = (latitude - prevLocation.first).pow(2)
        val lngDistance = (longitude - prevLocation.second).pow(2)
        return sqrt(latDistance + lngDistance) <= minDistance
    }
}