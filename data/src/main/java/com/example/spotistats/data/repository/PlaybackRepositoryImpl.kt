package com.example.spotistats.data.repository

import android.content.Context
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(
    private val config: SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    private val spotifyUserApi: SpotifyUserApi,
    private val topArtistsDao: TopArtistsDao,
    private val topTracksDao: TopTracksDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
):PlaybackRepository {
    override suspend fun getRecentlyPlayed(): RecentlyPlayed {
        val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getRecentlyPlayed("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun getCurrentlyPlaying(): CurrentlyPlaying {
        val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getCurrentlyPlaying("Bearer $token")
        return dto.toDomain()
    }

}