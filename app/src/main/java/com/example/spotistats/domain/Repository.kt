package com.example.spotistats.domain

import android.content.Intent
import com.example.spotistats.data.dto.AuthorizationDto
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.domain.model.RecentlyPlayed

interface Repository {
    fun buildAuthIntent(): Intent
    suspend fun exchangeCodeForToken(code: String): AuthorizationDto
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun getUserProfile(): UserProfileDto
    suspend fun getRecentlyPlayed(): RecentlyPlayed
    suspend fun clearAccessToken():Unit
}