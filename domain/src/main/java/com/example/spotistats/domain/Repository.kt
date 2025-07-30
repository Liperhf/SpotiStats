package com.example.spotistats.domain

import android.content.Intent
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks

interface Repository {
    fun buildAuthIntent(): Intent
    suspend fun exchangeCodeForToken(code: String): AuthToken
    suspend fun saveTokens(authTokenDto: AuthToken)
    suspend fun getAccessToken(): String?
    suspend fun getUserProfile(): UserProfile
    suspend fun getRefreshToken():String?
    suspend fun getExpiresAt():Long
    suspend fun isTokenExpired():Boolean
    suspend fun refreshToken(refreshToken:String):AuthToken
    suspend fun getRecentlyPlayed(): RecentlyPlayed
    suspend fun clearTokens():Unit
    suspend fun getCurrentlyPlaying():CurrentlyPlaying
    suspend fun getUserTopArtistsShort():UserTopArtists
    suspend fun getUserTopArtistsMedium():UserTopArtists
    suspend fun getUserTopArtistsLong():UserTopArtists
    suspend fun getUserTopTracksShort():UserTopTracks
    suspend fun getUserTopTracksMedium():UserTopTracks
    suspend fun getUserTopTracksLong():UserTopTracks
}