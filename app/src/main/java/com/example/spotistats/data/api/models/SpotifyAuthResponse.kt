package com.example.spotistats.data.api.models

data class SpotifyAuthResponse (
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String
)