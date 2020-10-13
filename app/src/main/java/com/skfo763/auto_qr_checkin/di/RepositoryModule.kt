package com.skfo763.auto_qr_checkin.di

import com.skfo763.repository.lockscreen.LockScreenRepository
import com.skfo763.repository.lockscreen.LockScreenRepositoryImpl
import com.skfo763.repository.lockscreen.MainRepositoryImpl
import com.skfo763.repository.main.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLockScreenRepository(impl: LockScreenRepositoryImpl): LockScreenRepository

    @Binds
    abstract fun bindMainRepository(impl: MainRepositoryImpl): MainRepository
}