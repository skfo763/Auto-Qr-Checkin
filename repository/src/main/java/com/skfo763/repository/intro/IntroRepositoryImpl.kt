package com.skfo763.repository.intro

import com.skfo763.remote.RealtimeDBManager
import com.skfo763.remote.data.IntroYoutube
import com.skfo763.storage.datastore.AppDataStore
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class IntroRepositoryImpl @Inject constructor(
    private val appDataStore: AppDataStore,
    private val realtimeDBManager: RealtimeDBManager
) : IntroRepository {

    @ExperimentalCoroutinesApi
    override fun getIntroVideoInfo(): Flow<IntroYoutube> {
        return realtimeDBManager.introVideoInfo
    }

    override suspend fun saveInitializeState(doInitialize: Boolean) {
        appDataStore.setInitSettingState(doInitialize)
    }

}