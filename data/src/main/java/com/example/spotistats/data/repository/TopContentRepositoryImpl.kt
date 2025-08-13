package com.example.spotistats.data.repository

import android.util.Log
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.dtoMappers.toDomain
import com.example.spotistats.data.mapper.entityMappers.toDomain
import com.example.spotistats.data.mapper.entityMappers.toEntity
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.TopContentRepository
import com.example.spotistats.domain.model.TimeRange
import javax.inject.Inject

class TopContentRepositoryImpl @Inject constructor(
    private val spotifyUserApi: SpotifyUserApi,
    private val topArtistsDao: TopArtistsDao,
    private val topTracksDao: TopTracksDao,
    private val authRepository: AuthRepository,
) : TopContentRepository {
    override suspend fun getUserTopArtists(timeRange: TimeRange): UserTopArtists {
        val rangeKey = timeRange.api
        return try {
            val token = authRepository.getAccessToken()
                ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopArtists(
                authorization = "Bearer $token",
                time_range = rangeKey
            )
            val domainModel = dto.toDomain()

            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(rangeKey, index + 1)
            }
            topArtistsDao.clearTopArtists(rangeKey)
            topArtistsDao.insertTopArtists(entities)

            domainModel
        } catch (e: Exception) {
            Log.e("RepositoryImpl", "Artists API error: ${e.message}", e)
            val cachedEntities = topArtistsDao.getTopArtists(rangeKey)
            if (cachedEntities.isEmpty()) {
                return UserTopArtists(emptyList())
            }
            UserTopArtists(items = cachedEntities.map { it.toDomain() })
        }
    }

    override suspend fun getUserTopTracks(timeRange: TimeRange): UserTopTracks {
        val rangeKey = timeRange.api
        return try {
            val token = authRepository.getAccessToken()
                ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopTracks(
                authorization = "Bearer $token",
                time_range = rangeKey
            )
            val domainModel = dto.toDomain()

            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(rangeKey, index + 1)
            }
            topTracksDao.clearTopTracks(rangeKey)
            topTracksDao.insertTopTracks(entities)

            domainModel
        } catch (e: Exception) {
            Log.e("RepositoryImpl", "Tracks API error: ${e.message}", e)
            val cachedEntities = topTracksDao.getTopTracks(rangeKey)
            if (cachedEntities.isEmpty()) {
                return UserTopTracks(emptyList())
            }
            UserTopTracks(items = cachedEntities.map { it.toDomain() })
        }
    }
}