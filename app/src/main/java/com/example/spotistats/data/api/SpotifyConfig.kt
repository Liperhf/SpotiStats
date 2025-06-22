package com.example.spotistats.data.api

object SpotifyConfig{
    const val CLIENT_ID = "e53821b64ad343178a194dc29fd59465"
    const val CLIENT_SECRET = "41a0238ce3fb4495831c59e34d202453"
    const val REDIRECT_URI = "spotistats://callback"
    const val AUTH_BASE_URL = "https://accounts.spotify.com/"
    const val API_BASE_URL = "https://api.spotify.com/v1/"
    val SCOPES = listOf(
        "user-read-recently-played",
        "user-read-currently-playing"
)
}