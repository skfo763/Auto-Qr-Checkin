package com.skfo763.repository.lockscreen

import com.skfo763.base.theme.ThemeType
import com.skfo763.remote.RealtimeDBManager
import com.skfo763.repository.BuildConfig
import com.skfo763.repository.model.CheckInType
import com.skfo763.repository.model.NaverCheckInUrl
import com.skfo763.repository.model.LanguageState
import com.skfo763.repository.model.qrCheckInType
import com.skfo763.storage.datastore.AppDataStore
import com.skfo763.storage.datastore.LockScreenDataStore
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityScoped
class LockScreenRepositoryImpl @Inject constructor(
    private val lockScreenDataStore: LockScreenDataStore,
    private val appDataStore: AppDataStore,
    private val realtimeDBManager: RealtimeDBManager
) : LockScreenRepository {

    @ExperimentalCoroutinesApi
    override fun getNaverQrCheckInUrl(): Flow<NaverCheckInUrl> {
        return realtimeDBManager.naverQrApiUrl.map {
            NaverCheckInUrl(it.url, it.availableHost, it.availablePath, it.appLandingScheme, it.errorList)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getKakaoQrCheckInUrl(): Flow<String> {
        return realtimeDBManager.kakaoQrApiUrl.map {
            return@map if(it.intentUrl != null && it.browseUrl != null) {
                it.intentUrl + "?url=" + it.browseUrl
            } else {
                BuildConfig.QR_CHECKIN_URL_KAKAO
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun getPlayStoreUrl(): Flow<String> {
        return realtimeDBManager.playStoreUrl
    }

    override fun getCurrentLockFeatureState(): Flow<Boolean> {
        return lockScreenDataStore.lockScreenStateFlow
    }

    override fun getCurrentWidgetFeatureState(): Flow<Boolean> {
        return lockScreenDataStore.widgetFeatureStateFlow
    }

    override fun getCurrentDeleteAdsState(): Flow<Boolean> {
        return appDataStore.deleteAdsStateFlow
    }

    override fun getCurrentAutoCheckinState(): Flow<Boolean> {
        return appDataStore.autoCheckInStateFlow
    }

    override fun getLanguageState(): Flow<LanguageState> {
        return appDataStore.languageFlow.map {
            mappingLanguageState(it)
        }
    }

    override fun getAppIconState(): Flow<String> {
        return appDataStore.appIconTypeFlow
    }

    override fun getCurrentQrCheckInType(): Flow<CheckInType> {
        return appDataStore.qrCheckinTypeFlow.map {
            it.qrCheckInType
        }
    }

    override fun getCurrentUiTheme(): Flow<ThemeType> {
        return appDataStore.uiThemeTypeFlow
    }

    override suspend fun setLockFeatureState(isFeatureOn: Boolean) {
        lockScreenDataStore.setLockScreenFeatureState(isFeatureOn)
    }

    override suspend fun setWidgetFeatureState(isFeatureOn: Boolean) {
        lockScreenDataStore.setWidgetFeatureState(isFeatureOn)
    }

    override suspend fun setDeleteAdsState(isFeatureOn: Boolean) {
        appDataStore.setDeleteAdsState(isFeatureOn)
    }

    override suspend fun setLanguageState(language: LanguageState) {
        appDataStore.setLanguage(language.value)
    }

    override suspend fun setAutoCheckInState(checked: Boolean) {
        appDataStore.setDoAutoCheckInState(checked)
    }

    override suspend fun resetInitializationState(doInitialize: Boolean) {
        appDataStore.setInitSettingState(doInitialize)
    }

    override suspend fun setAppIconType(type: String) {
        appDataStore.setAppIconType(type)
    }

    override suspend fun setQrCheckInType(type: CheckInType) {
        appDataStore.setQrCheckinType(type.type)
    }

    override suspend fun setCurrentUiTheme(type: ThemeType) {
        appDataStore.setCurrentUiTheme(type)
    }

    private val mappingLanguageState: (String) -> LanguageState = {
        when(it) {
            LanguageState.KOREAN.value -> LanguageState.KOREAN
            LanguageState.ENGLISH.value -> LanguageState.ENGLISH
            else -> LanguageState.KOREAN
        }
    }

}