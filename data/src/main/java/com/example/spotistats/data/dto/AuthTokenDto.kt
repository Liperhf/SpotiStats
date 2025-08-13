package com.example.spotistats.data.dto

data class AuthTokenDto (
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String
)