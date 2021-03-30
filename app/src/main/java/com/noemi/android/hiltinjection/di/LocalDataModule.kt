package com.noemi.android.hiltinjection.di

import com.noemi.android.hiltinjection.datasourcelocal.ArtDataSource
import com.noemi.android.hiltinjection.datasourcelocal.ArtLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class LocalDataModule {

    @Binds
    @Singleton
    abstract fun bindsLocaleData(impl: ArtLocalDataSource): ArtDataSource
}