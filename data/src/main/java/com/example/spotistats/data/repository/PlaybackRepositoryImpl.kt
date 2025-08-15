package com.example.spotistats.data.repository

import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.RecentlyPlayedDao
import com.example.spotistats.data.mapper.dtoMappers.toDomain
import com.example.spotistats.data.mapper.entityMappers.toDomain
import com.example.spotistats.data.mapper.entityMappers.toEntity
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.PlaybackRepository
import javax.inject.Inject

class PlaybackRepositoryImpl @Inject constructor(
    private val spotifyUserApi: SpotifyUserApi,
    private val authRepository: AuthRepository,
    private val recentlyPlayedDao: RecentlyPlayedDao,
) : PlaybackRepository {
    override suspend fun getRecentlyPlayed(): RecentlyPlayed {
        return try {
            val token = authRepository.getAccessToken()
                ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getRecentlyPlayed("Bearer $token")
            val domain = dto.toDomain()
            val entities = domain.tracks
                .take(50)
                .mapIndexed { index, track -> track.toEntity(index + 1) }
            recentlyPlayedDao.clearAll()
            if (entities.isNotEmpty()) {
                recentlyPlayedDao.insertAll(entities)
            }
            domain
        } catch (e: Exception) {
            val cached = recentlyPlayedDao.getAll()
            val tracks = cached.map { it.toDomain() }
            RecentlyPlayed(tracks = tracks)
        }
    }

    override suspend fun getCurrentlyPlaying(): CurrentlyPlaying {
        val token =
            authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getCurrentlyPlaying("Bearer $token")
        return dto.toDomain()
    }

}