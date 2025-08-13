package com.example.spotistats.domain.repository

import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.TimeRange

interface TopContentRepository {
    suspend fun getUserTopArtists(timeRange: TimeRange = TimeRange.SHORT): UserTopArtists
    suspend fun getUserTopTracks(timeRange: TimeRange = TimeRange.SHORT): UserTopTracks
}