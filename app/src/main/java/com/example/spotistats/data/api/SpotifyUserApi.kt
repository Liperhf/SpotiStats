package com.example.spotistats.data.api

import com.example.spotistats.data.api.models.SpotifyUserProfile
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyUserApi {
    @GET("me")
    suspend fun getUserProfile(
        @Header("Authorization") authorization:String
    ):SpotifyUserProfile
}