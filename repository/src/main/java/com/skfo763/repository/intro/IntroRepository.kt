package com.skfo763.repository.intro

import com.skfo763.remote.data.IntroYoutube
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun getIntroVideoInfo(): Flow<IntroYoutube>

    suspend fun saveInitializeState(doInitialize: Boolean)


}