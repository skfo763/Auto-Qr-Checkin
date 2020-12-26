package com.skfo763.repository.intro

import com.skfo763.remote.data.IntroYoutube
import com.skfo763.repository.model.CheckInType
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun getIntroVideoInfo(): Flow<IntroYoutube>

    fun getQrCheckInType(): Flow<CheckInType>

    suspend fun saveInitializeState(doInitialize: Boolean)

    suspend fun saveQrCheckInType(checkInType: CheckInType)

}