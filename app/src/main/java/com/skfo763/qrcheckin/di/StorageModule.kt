package com.skfo763.qrcheckin.di

import android.app.Application
import androidx.room.Room
import com.skfo763.storage.datastore.AppDataStore
import com.skfo763.storage.datastore.LockScreenDataStore
import com.skfo763.storage.room.CheckPointDatabase
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

        @Singleton
        @Provides
        fun provideCheckpointDatabase(application: Application): CheckPointDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                CheckPointDatabase::class.java,
                "db-checkpoint"
            ).build()
        }
    }

}