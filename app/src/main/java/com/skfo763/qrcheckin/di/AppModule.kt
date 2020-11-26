package com.skfo763.qrcheckin.di

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.skfo763.component.tracker.FirebaseTracker
import com.skfo763.qrcheckin.launch.LaunchActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseTracker(application: Application): FirebaseTracker {
        return FirebaseTracker(application)
    }

    @Provides
    fun provideLaunchIntent(application: Application): Intent {
        return Intent(application.applicationContext, LaunchActivity::class.java)
    }
}