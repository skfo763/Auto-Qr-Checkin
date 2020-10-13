package com.skfo763.repository.lockscreen

import com.skfo763.repository.main.MainRepository
import com.skfo763.storage.datastore.LockScreenDataStore
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class MainRepositoryImpl @Inject constructor(
    private val dataStore: LockScreenDataStore
): MainRepository {

    override fun getCurrentLockFeatureState(): Flow<Boolean> {
        return dataStore.lockScreenStateFlow
    }

    override suspend fun setLockFeatureState(isFeatureOn: Boolean) {
        dataStore.setLockScreenFeatureState(isFeatureOn)
    }


}