package com.example.spotistats.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.local.dao.TopArtistsDao
import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.mapper.DtoMappers.toDomain
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val config: SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    @ApplicationContext private val context: Context
) : AuthRepository {
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
}