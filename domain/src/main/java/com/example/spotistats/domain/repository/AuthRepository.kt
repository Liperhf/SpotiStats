package com.example.spotistats.domain.repository

import android.content.Intent
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks

interface AuthRepository {
    fun buildAuthIntent(): Intent
    suspend fun exchangeCodeForToken(code: String): AuthToken
    suspend fun saveTokens(authTokenDto: AuthToken)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken():String?
    suspend fun getExpiresAt():Long
    suspend fun isTokenExpired():Boolean
    suspend fun refreshToken(refreshToken:String):AuthToken
    suspend fun clearTokens():Unit

}