package com.skfo763.qrcheckin.di

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import com.skfo763.component.bixbysetting.BixbyLandingManager
import com.skfo763.component.bottomsheetdialog.MultiSelectDialog
import com.skfo763.component.playcore.InAppReviewManager
import com.skfo763.component.playcore.InAppUpdateManager
import com.skfo763.qrcheckin.admob.AdMobManager
import com.skfo763.qrcheckin.intro.adapter.IntroPagerAdapter
import com.skfo763.qrcheckin.launch.LaunchIconManager
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

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    companion object {
        @Provides
        fun provideInAppReviewManager(activity: Activity): InAppReviewManager {
            return InAppReviewManager(activity)
        }

        @Provides
        fun provideInAppUpdateManager(activity: Activity): InAppUpdateManager {
            return InAppUpdateManager(activity)
        }

        @Provides
        fun provideAdMobManager(activity: Activity): AdMobManager {
            return AdMobManager(activity)
        }

        @Provides
        fun provideLaunchIconManager(activity: Activity): LaunchIconManager {
            return LaunchIconManager(activity)
        }

        @Provides
        fun provideAppIconSelectDialog(activity: Activity): MultiSelectDialog {
            return MultiSelectDialog()
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

        @Provides
        fun provideBixbyLandingManager(activity: Activity): BixbyLandingManager {
            return BixbyLandingManager(activity)
        }

        @Provides
        fun provideIntroPagerAdapter(activity: FragmentActivity): IntroPagerAdapter {
            return IntroPagerAdapter(activity)
        }
    }
}


