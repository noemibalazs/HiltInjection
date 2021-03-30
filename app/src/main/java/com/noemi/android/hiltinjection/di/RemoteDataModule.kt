package com.noemi.android.hiltinjection.di

import com.noemi.android.hiltinjection.datasourceremote.ArtRemoteData
import com.noemi.android.hiltinjection.datasourceremote.ArtRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class RemoteDataModule {

    @Binds
    @Singleton
    abstract fun bindsRemoteData(impl: ArtRemoteDataSource): ArtRemoteData
}