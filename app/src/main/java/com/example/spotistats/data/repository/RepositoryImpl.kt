package com.example.spotistats.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.spotistats.data.api.SpotifyAuthApi
import com.example.spotistats.data.api.SpotifyConfig
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.dto.AuthorizationDto
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.data.mapper.toDomain
import com.example.spotistats.domain.Repository
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.Track
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

    override suspend fun exchangeCodeForToken(code: String): AuthorizationDto {
        return spotifyAuthApi.exchangeCodeForToken(code = code,
            clientId = config.CLIENT_ID,
            clientSecret = config.CLIENT_SECRET,
            redirectUri = config.REDIRECT_URI)
    }

    override suspend fun saveAccessToken(token: String) {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        prefs.edit().putString("access_token",token).apply()
    }

    override suspend fun getAccessToken(): String? {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        return prefs.getString("access_token",null)
    }

    override suspend fun getUserProfile(): UserProfileDto {
        val token = getAccessToken() ?: throw IllegalStateException("No access token")
        return spotifyUserApi.getUserProfile("Bearer $token")
    }

    override suspend fun getRecentlyPlayed(): RecentlyPlayed {
        val token = getAccessToken() ?: throw IllegalArgumentException("No access token")
        val dto = spotifyUserApi.getRecentlyPlayed("Bearer $token")
        return dto.toDomain()
    }

    override suspend fun clearAccessToken() {
        val prefs = context.getSharedPreferences("spotify_prefs",Context.MODE_PRIVATE)
        return prefs.edit().remove("access_token").apply()
    }
}
