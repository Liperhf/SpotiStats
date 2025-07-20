package com.example.spotistats.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.dto.AuthTokenDto
import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.data.mapper.toDomain
import com.example.spotistats.domain.Repository
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val config:SpotifyConfig,
    private val spotifyAuthApi: SpotifyAuthApi,
    private val spotifyUserApi: SpotifyUserApi,
    @ApplicationContext private val context: Context
) : Repository{
    override fun buildAuthIntent(): Intent {
        val clientId = config.CLIENT_ID
        val redirectUri = config.REDIRECT_URI
        val scopes = config.SCOPES.joinToString(" ")
        val state = generateRandomState()

        val url = Uri.Builder()
            .scheme("https")
            .authority("accounts.spotify.com")
            .path("authorize")
            .appendQueryParameter("client_id",clientId)
            .appendQueryParameter("response_type","code")
            .appendQueryParameter("redirect_uri",redirectUri)
            .appendQueryParameter("scope",scopes)
            .appendQueryParameter("state",state)
            .build()

        return Intent(Intent.ACTION_VIEW,url)
    }

    private fun generateRandomState(length:Int = 16) : String{
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length).map{chars.random()}.joinToString("")
    }

    override suspend fun exchangeCodeForToken(code: String): AuthTokenDto {
        return spotifyAuthApi.exchangeCodeForToken(code = code,
            clientId = config.CLIENT_ID,
            clientSecret = config.CLIENT_SECRET,
            redirectUri = config.REDIRECT_URI,)
    }

    override suspend fun saveTokens(authTokenDto: AuthTokenDto) {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        val expiresAt = System.currentTimeMillis() + authTokenDto.expires_in * 1000

        prefs.edit()
            .putString("access_token",authTokenDto.access_token)
            .putString("refresh_token",authTokenDto.refresh_token)
            .putLong("expires_at",expiresAt)
            .apply()
    }

    override suspend fun getRefreshToken(): String? {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        return prefs.getString("refresh_token",null)
    }

    override suspend fun refreshToken(refreshToken: String): AuthTokenDto {
        return spotifyAuthApi.refreshToken(
            refreshToken = refreshToken,
            clientSecret = config.CLIENT_SECRET,
            clientId = config.CLIENT_ID)
    }

    override suspend fun getExpiresAt(): Long {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        return prefs.getLong("expires_at",0)
    }

    override suspend fun getAccessToken(): String? {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        return prefs.getString("access_token",null)
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
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
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
        val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getTopArtistsShort("Bearer $token")
        println("DTO from API: $dto")
        return dto.toDomain()
    }

}
