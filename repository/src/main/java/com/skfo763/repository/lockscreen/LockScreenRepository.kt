package com.skfo763.repository.lockscreen

import com.skfo763.repository.model.CheckInType
import com.skfo763.repository.model.NaverCheckInUrl
import com.skfo763.repository.model.LanguageState
import kotlinx.coroutines.flow.Flow

interface LockScreenRepository {

    fun getNaverQrCheckInUrl(): Flow<NaverCheckInUrl>

    fun getKakaoQrCheckInUrl(): Flow<String>

    fun getPlayStoreUrl(): Flow<String>

    fun getCurrentLockFeatureState(): Flow<Boolean>

    fun getCurrentWidgetFeatureState(): Flow<Boolean>

    fun getCurrentDeleteAdsState(): Flow<Boolean>

    fun getCurrentAutoCheckinState(): Flow<Boolean>

    fun getLanguageState(): Flow<LanguageState>

    fun getAppIconState(): Flow<String>

    fun getCurrentQrCheckInType(): Flow<CheckInType>

    suspend fun setLockFeatureState(isFeatureOn: Boolean)

    suspend fun setWidgetFeatureState(isFeatureOn: Boolean)

    suspend fun setDeleteAdsState(isFeatureOn: Boolean)

    suspend fun setLanguageState(language: LanguageState)

    suspend fun setAutoCheckInState(checked: Boolean)

    suspend fun resetInitializationState(doInitialize: Boolean)

    suspend fun setAppIconType(type: String)

}