package com.example.spotistats.data.repository

import android.content.Context
import android.util.Log
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.data.mapper.EntityMappers.toDomain
import com.example.spotistats.data.mapper.EntityMappers.toEntity
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.TopContentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class  TopContentRepositoryImpl @Inject constructor(
    private val config: SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    private val spotifyUserApi: SpotifyUserApi,
    private val topArtistsDao: TopArtistsDao,
    private val topTracksDao: TopTracksDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
):TopContentRepository {
    override suspend fun getUserTopArtistsShort(): UserTopArtists {
        val timeRange = "short_term"

        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopArtistsShort("Bearer $token")
            val domainModel = dto.toDomain()

            val entities = domainModel.items.mapIndexed { index, item ->
                val entity = item.toEntity(timeRange, index + 1)
                if (entity == null) {
                    Log.w("RepositoryImpl", "Entity mapping returned null for index $index: $item")
                }
                entity
            }.filterNotNull()


            topArtistsDao.clearTopArtists(timeRange)

            topArtistsDao.insertTopArtists(entities)

            domainModel

        } catch (e: Exception) {
            Log.e("RepositoryImpl", "API error: ${e.message}", e)

            val cachedEntities = topArtistsDao.getTopArtists(timeRange)
            Log.d("RepositoryImpl", "Loaded ${cachedEntities.size} artists from DB cache")

            if (cachedEntities.isEmpty()) {
                return UserTopArtists(emptyList())
            }

            val domainFromCache = UserTopArtists(items = cachedEntities.map { it.toDomain() })
            Log.d("RepositoryImpl", "Returning cached UserTopArtists with ${domainFromCache.items.size} items")

            domainFromCache
        }
    }



    override suspend fun getUserTopArtistsMedium(): UserTopArtists {
        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopArtistsMedium("Bearer $token")
            val domainModel = dto.toDomain()
            val timeRange = "medium_term"
            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(timeRange, index + 1)
            }.filterNotNull()

            topArtistsDao.clearTopArtists(timeRange)
            topArtistsDao.insertTopArtists(entities)

            domainModel
        } catch (e: Exception) {
            val cachedEntities = topArtistsDao.getTopArtists("medium_term")
            if (cachedEntities.isEmpty()) {
                return UserTopArtists(emptyList())
            }
            UserTopArtists(items = cachedEntities.map { it.toDomain() })
        }
    }

    override suspend fun getUserTopArtistsLong(): UserTopArtists {
        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopArtistsLong("Bearer $token")
            val domainModel = dto.toDomain()
            val timeRange = "long_term"
            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(timeRange, index + 1)
            }.filterNotNull()

            topArtistsDao.clearTopArtists(timeRange)
            topArtistsDao.insertTopArtists(entities)

            domainModel
        } catch (e: Exception) {
            val cachedEntities = topArtistsDao.getTopArtists("long_term")
            if (cachedEntities.isEmpty()) {
                return UserTopArtists(emptyList())
            }
            UserTopArtists(items = cachedEntities.map { it.toDomain() })
        }
    }

    override suspend fun getUserTopTracksShort(): UserTopTracks {
        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopTracksShort("Bearer $token")
            val domainModel = dto.toDomain()
            val timeRange = "short_term"
            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(timeRange, index + 1)
            }.filterNotNull()

            topTracksDao.clearTopTracks(timeRange)
            topTracksDao.insertTopTracks(entities)

            domainModel
        } catch (e: Exception) {
            val cachedEntities = topTracksDao.getTopTracks("short_term")
            if (cachedEntities.isEmpty()) {
                return UserTopTracks(emptyList())
            }
            UserTopTracks(items = cachedEntities.map { it.toDomain() })
        }
    }

    override suspend fun getUserTopTracksMedium(): UserTopTracks {
        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopTracksMedium("Bearer $token")
            val domainModel = dto.toDomain()
            val timeRange = "medium_term"
            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(timeRange, index + 1)
            }.filterNotNull()

            topTracksDao.clearTopTracks(timeRange)
            topTracksDao.insertTopTracks(entities)

            domainModel
        } catch (e: Exception) {
            val cachedEntities = topTracksDao.getTopTracks("medium_term")
            if (cachedEntities.isEmpty()) {
                return UserTopTracks(emptyList())
            }
            UserTopTracks(items = cachedEntities.map { it.toDomain() })
        }
    }

    override suspend fun getUserTopTracksLong(): UserTopTracks {
        return try {
            val token = authRepository.getAccessToken() ?: throw IllegalArgumentException("No access token")
            val dto = spotifyUserApi.getTopTracksLong("Bearer $token")
            val domainModel = dto.toDomain()
            val timeRange = "long_term"
            val entities = domainModel.items.mapIndexed { index, item ->
                item.toEntity(timeRange, index + 1)
            }.filterNotNull()

            topTracksDao.clearTopTracks(timeRange)
            topTracksDao.insertTopTracks(entities)

            domainModel
        } catch (e: Exception) {
            val cachedEntities = topTracksDao.getTopTracks("long_term")
            if (cachedEntities.isEmpty()) {
                return UserTopTracks(emptyList())
            }
            UserTopTracks(items = cachedEntities.map { it.toDomain() })
        }
    }
}