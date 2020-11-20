package com.skfo763.qrcheckin.di

import android.app.Application
import com.skfo763.component.tracker.FirebaseTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseTracker(application: Application): FirebaseTracker {
        return FirebaseTracker(application)
    }
}