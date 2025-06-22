package com.example.spotistats.domain

import android.content.Intent
import com.example.spotistats.data.dto.AuthTokenDto
import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile

interface Repository {
    fun buildAuthIntent(): Intent
    suspend fun exchangeCodeForToken(code: String): AuthTokenDto
    suspend fun saveTokens(authTokenDto: AuthTokenDto)
    suspend fun getAccessToken(): String?
    suspend fun getUserProfile(): UserProfile
    suspend fun getRefreshToken():String?
    suspend fun getExpiresAt():Long
    suspend fun isTokenExpired():Boolean
    suspend fun refreshToken(refreshToken:String):AuthTokenDto
    suspend fun getRecentlyPlayed(): RecentlyPlayed
    suspend fun clearTokens():Unit
    suspend fun getCurrentlyPlaying():CurrentlyPlaying
}