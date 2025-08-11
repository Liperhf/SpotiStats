package com.example.spotistats.data.di

import com.example.spotistats.data.repository.AuthRepositoryImpl
import com.example.spotistats.data.repository.PlaybackRepositoryImpl
import com.example.spotistats.data.repository.TopContentRepositoryImpl
import com.example.spotistats.data.repository.UserRepositoryImpl
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import com.example.spotistats.domain.repository.TopContentRepository
import com.example.spotistats.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTopContentRepository(topContentRepositoryImpl: TopContentRepositoryImpl): TopContentRepository

    @Binds
    @Singleton
    abstract fun bindPlaybackRepository(playbackRepositoryImpl: PlaybackRepositoryImpl): PlaybackRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}