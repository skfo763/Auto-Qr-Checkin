package com.skfo763.qrcheckin.di

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.skfo763.component.playcore.InAppReviewManager
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.lockscreen.receiver.AddressResultReceiver
import com.skfo763.remote.NetworkManager
import com.skfo763.remote.api.NaverMapApi
import com.skfo763.storage.gps.GpsManager
import com.skfo763.storage.room.CheckPointDao
import com.skfo763.storage.room.CheckPointDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

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

        @Provides
        fun provideNaverMapApi(): NaverMapApi {
            return NetworkManager().getNaverApiRetrofit().create(NaverMapApi::class.java)
        }

        @Provides
        fun provideCheckpointDao(db: CheckPointDatabase): CheckPointDao {
            return db.checkPointDao()
        }

        @Provides
        fun provideAddressResultReceiver(): AddressResultReceiver {
            return AddressResultReceiver(Handler(Looper.getMainLooper()))
        }
    }
}


