package com.skfo763.qrcheckin.di

import com.skfo763.repository.checkinmap.CheckInMapRepository
import com.skfo763.repository.checkinmap.CheckInMapRepositoryImpl
import com.skfo763.repository.intro.IntroRepository
import com.skfo763.repository.intro.IntroRepositoryImpl
import com.skfo763.repository.lockscreen.LockScreenRepository
import com.skfo763.repository.lockscreen.LockScreenRepositoryImpl
import com.skfo763.repository.lockscreen.MainRepositoryImpl
import com.skfo763.repository.main.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLockScreenRepository(impl: LockScreenRepositoryImpl): LockScreenRepository

    @Binds
    abstract fun bindMainRepository(impl: MainRepositoryImpl): MainRepository

    @Binds
    abstract fun bindCheckInMapRepository(impl: CheckInMapRepositoryImpl): CheckInMapRepository

    @Binds
    abstract fun bindIntroRepository(impl: IntroRepositoryImpl): IntroRepository

}