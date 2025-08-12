package com.example.spotistats.data.api

import com.example.data.BuildConfig


object SpotifyConfig {
    const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
    const val CLIENT_SECRET = BuildConfig.SPOTIFY_CLIENT_SECRET
    const val REDIRECT_URI = BuildConfig.SPOTIFY_REDIRECT_URI
    const val AUTH_BASE_URL = "https://accounts.spotify.com/"
    const val API_BASE_URL = "https://api.spotify.com/v1/"
    val SCOPES = listOf(
        "user-read-recently-played",
        "user-read-currently-playing",
        "user-top-read"
    )
}