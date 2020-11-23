package com.skfo763.repository.intro

import com.skfo763.storage.datastore.AppDataStore
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val appDataStore: AppDataStore
) : IntroRepository {

}