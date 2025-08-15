package com.example.spotistats.data.di

import android.content.Context
import androidx.room.Room
import com.example.spotistats.data.local.AppDataBase
import com.example.spotistats.data.local.dao.RecentlyPlayedDao
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "spotistats.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideTopArtistsDao(appDataBase: AppDataBase): TopArtistsDao {
        return appDataBase.topArtistsDao()
    }

    @Provides
    @Singleton
    fun provideTopTracksDao(appDataBase: AppDataBase): TopTracksDao {
        return appDataBase.topTracksDao()
    }

    @Provides
    @Singleton
    fun provideRecentlyPlayedDao(appDataBase: AppDataBase): RecentlyPlayedDao {
        return appDataBase.recentlyPlayedDao()
    }
}