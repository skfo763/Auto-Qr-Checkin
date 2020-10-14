package com.skfo763.qrcheckin.di

import com.skfo763.remote.RealtimeDBManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RemoteModule {

    companion object {
        @Singleton
        @Provides
        fun provideLockScreenDataStore(): RealtimeDBManager {
            return RealtimeDBManager()
        }
    }

}