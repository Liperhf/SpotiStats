package com.example.spotistats.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.dto.AuthTokenDto
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.data.mapper.EntityMappers.toDomain
import com.example.spotistats.data.mapper.EntityMappers.toEntity
import com.example.spotistats.domain.Repository
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val config:SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    private val spotifyUserApi: SpotifyUserApi,
    private val topArtistsDao:TopArtistsDao,
    private val topTracksDao:TopTracksDao,
    @ApplicationContext private val context: Context
) : Repository {
    override fun buildAuthIntent(): Intent {
        val clientId = config.CLIENT_ID
        val redirectUri = config.REDIRECT_URI
        val scopes = config.SCOPES.joinToString(" ")
        val state = generateRandomState()

        val url = Uri.Builder()
            .scheme("https")
            .authority("accounts.spotify.com")
            .path("authorize")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("scope", scopes)
            .appendQueryParameter("state", state)
            .build()

        return Intent(Intent.ACTION_VIEW, url)
    }

    private fun generateRandomState(length: Int = 16): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length).map { chars.random() }.joinToString("")
    }

    override suspend fun exchangeCodeForToken(code: String): AuthToken {
        return spotifyAuthApi.exchangeCodeForToken(
            code = code,
            clientId = config.CLIENT_ID,
            clientSecret = config.CLIENT_SECRET,
            redirectUri = config.REDIRECT_URI,
        ).toDomain()
    }

    override suspend fun saveTokens(authToken: AuthToken) {
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        val expiresAt = System.currentTimeMillis() + authToken.expires_in * 1000

        prefs.edit()
            .putString("access_token", authToken.access_token)
            .putString("refresh_token", authToken.refresh_token)
            .putLong("expires_at", expiresAt)
            .apply()
    }

    override suspend fun getRefreshToken(): String? {
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        return prefs.getString("refresh_token", null)
    }

    override suspend fun refreshToken(refreshToken: String): AuthToken {
        return spotifyAuthApi.refreshToken(
            refreshToken = refreshToken,
            clientSecret = config.CLIENT_SECRET,
            clientId = config.CLIENT_ID
        ).toDomain()
    }

    override suspend fun getExpiresAt(): Long {
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("expires_at", 0)
    }

    override suspend fun getAccessToken(): String? {
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        return prefs.getString("access_token", null)
    }

    override suspend fun getUserProfile(): UserProfile {
        val token = getAccessToken() ?: throw IllegalStateException("No access token")
        val dto = spotifyUserApi.getUserProfile("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun getRecentlyPlayed(): RecentlyPlayed {
        val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getRecentlyPlayed("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun isTokenExpired(): Boolean {
        val expiresAt = getExpiresAt()
        val currentTime = System.currentTimeMillis()
        return currentTime > expiresAt
    }

    override suspend fun clearTokens() {
        val prefs = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
        return prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .remove("expires_at")
            .apply()
    }

    override suspend fun getCurrentlyPlaying(): CurrentlyPlaying {
        val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getCurrentlyPlaying("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun getUserTopArtistsShort(): UserTopArtists {
        val timeRange = "short_term"

        return try {
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
            Log.d("RepositoryImpl", "Fetching top artists (short) with token: $token")

            // 1. Загружаем с сервера
            val dto = spotifyUserApi.getTopArtistsShort("Bearer $token")
            val domainModel = dto.toDomain()
            Log.d("RepositoryImpl", "API returned ${domainModel.items.size} artists")

            // 2. Конвертируем в Entity
            val entities = domainModel.items.mapIndexed { index, item ->
                val entity = item.toEntity(timeRange, index + 1)
                if (entity == null) {
                    Log.w("RepositoryImpl", "Entity mapping returned null for index $index: $item")
                }
                entity
            }.filterNotNull()

            Log.d("RepositoryImpl", "Mapped ${entities.size} artists to entities")

            // 3. Сохраняем в Room
            topArtistsDao.clearTopArtists(timeRange)
            Log.d("RepositoryImpl", "Cleared DB for timeRange=$timeRange")

            topArtistsDao.insertTopArtists(entities)
            Log.d("RepositoryImpl", "Inserted ${entities.size} artists into DB")

            // 4. Возвращаем Domain-модель
            domainModel

        } catch (e: Exception) {
            Log.e("RepositoryImpl", "API error: ${e.message}", e)

            // 5. Пытаемся загрузить из Room
            val cachedEntities = topArtistsDao.getTopArtists(timeRange)
            Log.d("RepositoryImpl", "Loaded ${cachedEntities.size} artists from DB cache")

            if (cachedEntities.isEmpty()) {
                return UserTopArtists(emptyList())
            }

            // 6. Конвертируем Entity -> Domain
            val domainFromCache = UserTopArtists(items = cachedEntities.map { it.toDomain() })
            Log.d("RepositoryImpl", "Returning cached UserTopArtists with ${domainFromCache.items.size} items")

            domainFromCache
        }
    }



    override suspend fun getUserTopArtistsMedium(): UserTopArtists {
        return try {
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
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
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
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
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
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
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
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
            val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
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
