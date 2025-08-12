package com.example.spotistats.data.repository

import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(
    private val spotifyUserApi: SpotifyUserApi,
    private val authRepository: AuthRepository,
) : PlaybackRepository {
    override suspend fun getRecentlyPlayed(): RecentlyPlayed {
        val token =
            authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getRecentlyPlayed("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun getCurrentlyPlaying(): CurrentlyPlaying {
        val token =
            authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getCurrentlyPlaying("Bearer $token")
        return dto.toDomain()
    }

}