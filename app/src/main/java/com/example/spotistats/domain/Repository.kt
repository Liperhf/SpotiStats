package com.example.spotistats.domain

import android.content.Intent
import com.example.spotistats.data.api.models.SpotifyAuthResponse
import com.example.spotistats.data.api.models.SpotifyUserProfile
import com.example.spotistats.data.dto.TrackDto

interface Repository {
    fun buildAuthIntent(): Intent
    suspend fun exchangeCodeForToken(code:String):SpotifyAuthResponse
    suspend fun saveAccessToken(token:String)
    suspend fun getAccessToken():String?
    suspend fun getUserProfile():SpotifyUserProfile
}