package com.example.spotistats.data.api

import CurrentlyPlayingDto
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.data.dto.UserTopArtistDto
import com.example.spotistats.data.dto.UserTopTracksDto
import com.example.spotistats.domain.model.UserTopTracks
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyUserApi {
    @GET("me")
    suspend fun getUserProfile(
        @Header("Authorization") authorization:String
    ): UserProfileDto

    @GET ("me/player/recently-played")
    suspend fun getRecentlyPlayed(
        @Header("Authorization") authorization: String,
        @Query("limit") limit:Int = 20
    ): RecentlyPlayedDto

    @GET ("me/player/currently-playing")
    suspend fun getCurrentlyPlaying(
        @Header("Authorization") authorization: String,
    ):CurrentlyPlayingDto

    @GET("me/top/artists")
    suspend fun getTopArtistsShort(
        @Header("Authorization") authorization: String,
        @Query("limit") limit:Int = 50,
        @Query("time_range") time_range:String = "short_term"
    ):UserTopArtistDto

    @GET("me/top/tracks")
    suspend fun getTopTracksShort(
        @Header("Authorization") authorization: String,
        @Query("limit") limit:Int = 50,
        @Query("time_range") time_range: String = "short_term"
    ):UserTopTracksDto
}