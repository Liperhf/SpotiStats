package com.example.spotistats.presentation.stats

import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks

data class StatsUiState(
    val selectedContentType: ContentType = ContentType.TRACKS,
    val selectedTimeRange: TimeRange = TimeRange.SHORT,
    val topTracks: UserTopTracks? = null,
    val topArtists: UserTopArtists? = null,
    val topAlbums: List<TopAlbum?> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class ContentType { TRACKS, ARTISTS, ALBUMS }
enum class TimeRange { SHORT, MEDIUM, LONG }