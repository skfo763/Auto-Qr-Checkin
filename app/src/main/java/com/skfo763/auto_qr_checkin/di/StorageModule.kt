package com.skfo763.auto_qr_checkin.di

import android.app.Application
import com.skfo763.storage.datastore.AppDataStore
import com.skfo763.storage.datastore.LockScreenDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class StorageModule {

    companion object {

        @Singleton
        @Provides
        fun provideLockScreenDataStore(application: Application): LockScreenDataStore {
            return LockScreenDataStore(application.applicationContext)
        }

        @Singleton
        @Provides
        fun provideAppDataStore(application: Application): AppDataStore {
            return AppDataStore(application.applicationContext)
        }
    }

}