package com.noemi.android.hiltinjection.di

import android.content.Context
import androidx.room.Room
import com.noemi.android.hiltinjection.room.ArtDAO
import com.noemi.android.hiltinjection.room.ArtDataBase
import com.noemi.android.hiltinjection.util.ART_DATA_BASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ArtDataBaseModule {

    @Provides
    @Singleton
    fun provideArtDataBase(@ApplicationContext context: Context): ArtDataBase {
        return Room.databaseBuilder(context, ArtDataBase::class.java, ART_DATA_BASE).build()
    }

    @Provides
    @Singleton
    fun provideArtDao(artDataBase: ArtDataBase): ArtDAO {
        return artDataBase.artDao()
    }
}