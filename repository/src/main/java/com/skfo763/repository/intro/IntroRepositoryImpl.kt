package com.skfo763.repository.intro

import com.skfo763.storage.datastore.AppDataStore
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class IntroRepositoryImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : IntroRepository {

    override suspend fun saveInitializeState(doInitialize: Boolean) {
        appDataStore.setInitSettingState(doInitialize)
    }

}