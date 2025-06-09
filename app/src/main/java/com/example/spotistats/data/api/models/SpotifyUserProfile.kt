package com.example.spotistats.data.api.models

data class SpotifyUserProfile (
    val id: String,
    val display_name: String,
    val email: String,
    val images: List<SpotifyImage>,
    val country: String
)