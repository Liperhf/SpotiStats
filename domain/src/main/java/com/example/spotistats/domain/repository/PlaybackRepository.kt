package com.example.spotistats.domain.repository

import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed

interface PlaybackRepository {
    suspend fun getRecentlyPlayed(): RecentlyPlayed
    suspend fun getCurrentlyPlaying(): CurrentlyPlaying
}