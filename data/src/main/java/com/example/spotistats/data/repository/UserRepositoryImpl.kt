package com.example.spotistats.data.repository

import android.content.Context
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val config: SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    private val spotifyUserApi: SpotifyUserApi,
    private val topArtistsDao: TopArtistsDao,
    private val topTracksDao: TopTracksDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
): UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val token = authRepository.getAccessToken() ?: throw IllegalStateException("No access token")
        val dto = spotifyUserApi.getUserProfile("Bearer $token")
        return dto.toDomain()
    }
}