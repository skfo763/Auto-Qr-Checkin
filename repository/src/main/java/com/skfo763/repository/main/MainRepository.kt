package com.skfo763.repository.main

import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getCurrentLockFeatureState(): Flow<Boolean>

    suspend fun setLockFeatureState(isFeatureOn: Boolean)

}