package com.example.spotistats.data.di

import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideSpotifyAuthRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(SpotifyConfig.AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    @Named("apiRetrofit")
    fun provideSpotifyApiRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl((SpotifyConfig.API_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideSpotifyAuthApi(@Named("authRetrofit") retrofit: Retrofit): SpotifyAuthApi {
        return retrofit.create(SpotifyAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyUserApi(@Named("apiRetrofit") retrofit: Retrofit): SpotifyUserApi {
        return retrofit.create(SpotifyUserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSpotifyConfig(): SpotifyConfig {
        return SpotifyConfig
    }
}