package com.skfo763.qrcheckin.di

import android.app.Activity
import android.app.Application
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.component.playcore.InAppReviewManager
import com.skfo763.component.tracker.FirebaseTracker
import com.skfo763.storage.gps.GpsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AppActivityModule {

    companion object {
        @Provides
        fun provideInAppReviewManager(activity: Activity): InAppReviewManager {
            return InAppReviewManager(activity)
        }

        @Provides
        fun provideAdMobManager(activity: Activity): AdMobManager {
            return AdMobManager(activity)
        }

        @Provides
        fun provideGpsManager(activity: Activity): GpsManager {
            return GpsManager(activity)
        }
    }

}

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseTracker(application: Application): FirebaseTracker {
        return FirebaseTracker(application)
    }
}