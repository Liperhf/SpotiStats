package com.example.spotistats.di

import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.api.models.SpotifyAuthResponse
import com.example.spotistats.data.repository.RepositoryImpl
import com.example.spotistats.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object Module{
    @Provides
    @Singleton
    fun provideSpotifyAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SpotifyConfig.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyApiRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl((SpotifyConfig.API_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthApi(retrofit: Retrofit):SpotifyAuthApi{
        return retrofit.create(SpotifyAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyUserApi(retrofit: Retrofit):SpotifyUserApi{
        return retrofit.create(SpotifyUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(repositoryImpl: RepositoryImpl): Repository = repositoryImpl

}   