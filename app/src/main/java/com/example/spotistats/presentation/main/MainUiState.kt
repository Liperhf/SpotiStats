package com.example.spotistats.presentation.main

import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import kotlinx.coroutines.Job


data class MainUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,

    val userProfile: UserProfile? = null,
    val currentlyPlaying: CurrentlyPlaying? = null,
    val recentlyPlayed: RecentlyPlayed? = null,
    val topArtists: UserTopArtists? = null,
    val topTracks: UserTopTracks? = null,
    val topAlbums: List<TopAlbum> = emptyList(),

    val progressMs: Int = 0,
) {
    val currentTrack = currentlyPlaying?.item
    val isCurrentlyPlaying = currentlyPlaying?.is_playing == true
    val lastPlayedTrack = recentlyPlayed?.tracks?.firstOrNull()

    val nowPlayingData: NowPlayingData? = if (isCurrentlyPlaying && currentTrack != null) {
        NowPlayingData(
            imageUrl = currentTrack.imageUrl,
            trackName = currentTrack.name,
            artistName = currentTrack.artists,
            durationMs = currentTrack.duration_ms,
            progressMs = progressMs
        )
    } else null

    val lastPlayedData: LastPlayedData? = lastPlayedTrack?.let { track ->
        LastPlayedData(
            imageUrl = track.imageUrl,
            trackName = track.name,
            artistName = track.artists
        )
    }
}

data class NowPlayingData(
    val imageUrl: String,
    val trackName: String,
    val artistName: String,
    val durationMs: Int,
    val progressMs: Int
)

data class LastPlayedData(
    val imageUrl: String,
    val trackName: String,
    val artistName: String
)