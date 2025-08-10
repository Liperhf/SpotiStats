package com.example.spotistats.domain.repository

import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks

interface TopContentRepository {
    suspend fun getUserTopArtistsShort(): UserTopArtists
    suspend fun getUserTopArtistsMedium(): UserTopArtists
    suspend fun getUserTopArtistsLong(): UserTopArtists
    suspend fun getUserTopTracksShort(): UserTopTracks
    suspend fun getUserTopTracksMedium(): UserTopTracks
    suspend fun getUserTopTracksLong(): UserTopTracks
}