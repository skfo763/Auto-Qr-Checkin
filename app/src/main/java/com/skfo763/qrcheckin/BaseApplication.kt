package com.skfo763.qrcheckin

import android.app.Application
import com.facebook.stetho.Stetho
import com.skfo763.base.theme.applyTheme
import com.skfo763.storage.datastore.AppDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject lateinit var appDataStore: AppDataStore

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        GlobalScope.launch {
            getApplicationThemeState()
        }
    }

    private suspend fun getApplicationThemeState() = appDataStore.uiThemeTypeFlow.collect {
        withContext(Dispatchers.Main) {
            applyTheme(it)
        }
    }

}