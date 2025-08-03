package com.example.spotistats.presentation.main.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.spotistats.presentation.main.MainUiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Job

@Composable
fun MainContent(isRefreshing: MutableState<Boolean>,
                refreshRecentlyPlayed: () -> Job,
                paddingValues: PaddingValues,
                onRecentlyShowClick:()->Unit,
                mainUiState: MainUiState,
                ){
    val currentlyImageUrl = mainUiState.nowPlayingData?.imageUrl
    val currentlyTrackName = mainUiState.nowPlayingData?.trackName
    val currentlyArtistName = mainUiState.nowPlayingData?.artistName
    val currentlyDurationMs = mainUiState.nowPlayingData?.durationMs
    val lastPlayedTrack = mainUiState.lastPlayedTrack
    val lastPlayedImageUrl = mainUiState.lastPlayedData?.imageUrl
    val lastPlayedName = mainUiState.lastPlayedData?.trackName
    val lastPlayedArtist = mainUiState.lastPlayedData?.artistName
    val recentlyPlayedTracks = mainUiState.recentlyPlayed?.tracks
    val userTopTracks = mainUiState.topTracks
    val userTopAlbums = mainUiState.topAlbums
    val userTopArtists = mainUiState.topArtists

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
        onRefresh = { refreshRecentlyPlayed() },
    ) {
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                when {
                    mainUiState.currentlyPlaying?.is_playing == true -> {
                        if (currentlyImageUrl != null && currentlyTrackName != null && currentlyArtistName != null) {
                            if (currentlyDurationMs != null) {
                                NowPlayingBox(
                                    currentlyImageUrl,
                                    currentlyTrackName,
                                    currentlyArtistName,
                                    currentlyDurationMs,
                                    mainUiState.progressMs
                                )
                            }
                        }
                    }

                    lastPlayedTrack != null -> {
                        if (lastPlayedImageUrl != null && lastPlayedName != null && lastPlayedArtist != null) {
                            LastPlayedBox(
                                lastPlayedImageUrl,
                                lastPlayedName,
                                lastPlayedArtist
                            )
                        }
                    }
                }
            }
            item {
                if (recentlyPlayedTracks != null) {
                    RecentlyPlayedBox(
                        recentlyPlayedTracks = recentlyPlayedTracks,
                        onShowAllClick = {onRecentlyShowClick()}
                    )
                }
            }
            item {
                userTopArtists.let {
                    if (it != null) {
                        UserTopArtistsBox(it)
                    }
                }
            }
            item {
                userTopTracks.let {
                    if (it != null) {
                        UserTopTracksBox(it)
                    }
                }
            }
            item {
                UserTopAlbumsBox(userTopAlbums)
            }
        }

    }
}