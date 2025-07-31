package com.example.spotistats.di

import android.content.Context
import androidx.room.Room
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.AppDataBase
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.repository.AuthRepositoryImpl
import com.example.spotistats.data.repository.PlaybackRepositoryImpl
import com.example.spotistats.data.repository.TopContentRepositoryImpl
import com.example.spotistats.data.repository.UserRepositoryImpl
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import com.example.spotistats.domain.repository.TopContentRepository
import com.example.spotistats.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)

object Module{
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideSpotifyAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SpotifyConfig.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("apiRetrofit")
    fun provideSpotifyApiRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl((SpotifyConfig.API_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthApi(@Named("authRetrofit") retrofit: Retrofit):SpotifyAuthApi{
        return retrofit.create(SpotifyAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyUserApi(@Named("apiRetrofit") retrofit: Retrofit):SpotifyUserApi{
        return retrofit.create(SpotifyUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository = authRepositoryImpl

    @Provides
    @Singleton
    fun provideTopContentRepository(topContentRepositoryImpl: TopContentRepositoryImpl): TopContentRepository = topContentRepositoryImpl

    @Provides
    @Singleton
    fun providePlaybackRepository(playbackRepositoryImpl: PlaybackRepositoryImpl): PlaybackRepository = playbackRepositoryImpl

    @Provides
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository = userRepositoryImpl

    @Provides
    @Singleton
    fun provideSpotifyConfig(): SpotifyConfig {
        return SpotifyConfig
    }

    @Provides
    @Singleton
    fun provideAppDataBase(@ApplicationContext context: Context):AppDataBase{
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
    fun provideTopArtistsDao(appDataBase:AppDataBase):TopArtistsDao{
        return appDataBase.topArtistsDao()
    }

    @Provides
    @Singleton
    fun provideTopTracksDao(appDataBase: AppDataBase):TopTracksDao{
        return appDataBase.topTracksDao()
    }
}   