package com.skfo763.repository.lockscreen

import android.location.Location
import com.skfo763.repository.model.CheckInUrl
import com.skfo763.repository.model.LanguageState
import kotlinx.coroutines.flow.Flow

interface LockScreenRepository {

    fun getNaverQrCheckInUrl(): Flow<CheckInUrl>

    fun getPlayStoreUrl(): Flow<String>

    fun getCurrentLockFeatureState(): Flow<Boolean>

    fun getCurrentWidgetFeatureState(): Flow<Boolean>

    fun getCurrentDeleteAdsState(): Flow<Boolean>

    fun getLanguageState(): Flow<LanguageState>

    fun getLastKnownLocation(): Flow<Location?>

    suspend fun setLockFeatureState(isFeatureOn: Boolean)

    suspend fun setWidgetFeatureState(isFeatureOn: Boolean)

    suspend fun setDeleteAdsState(isFeatureOn: Boolean)

    suspend fun setLanguageState(language: LanguageState)

}