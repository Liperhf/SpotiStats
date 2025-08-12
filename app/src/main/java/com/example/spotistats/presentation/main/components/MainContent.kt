package com.example.spotistats.presentation.main.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.CurrentlyItem
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.Track
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.UserTopTracksItem
import com.example.spotistats.presentation.main.MainUiState
import com.example.spotistats.ui.theme.SpotiStatsTheme
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

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun MainContentPreview(){
    SpotiStatsTheme {
        val image1 = "https://picsum.photos/seed/album1/300/300"
        val image2 = "https://picsum.photos/seed/album2/300/300"
        val image3 = "https://picsum.photos/seed/album3/300/300"

        val nowPlaying = CurrentlyPlaying(
            is_playing = true,
            item = CurrentlyItem(
                artists = "Artist Now",
                duration_ms = 230_000,
                id = "track_now",
                name = "Now Playing Track",
                imageUrl = image1
            ),
            progress_ms = 75_000
        )

        val recently = RecentlyPlayed(
            tracks = listOf(
                Track(
                    album = "Recent Album",
                    artists = "Recent Artist",
                    duration_ms = 210_000,
                    id = "recent_1",
                    name = "Recent Track 1",
                    type = "track",
                    uri = "spotify:track:recent_1",
                    imageUrl = image2
                ),
                Track(
                    album = "Recent Album 2",
                    artists = "Recent Artist 2",
                    duration_ms = 205_000,
                    id = "recent_2",
                    name = "Recent Track 2",
                    type = "track",
                    uri = "spotify:track:recent_2",
                    imageUrl = image3
                )
            )
        )

        val topArtists = UserTopArtists(
            items = listOf(
                UserTopArtistsItem(
                    images = listOf(Image(height = 300, url = image1, width = 300)),
                    name = "Top Artist 1"
                ),
                UserTopArtistsItem(
                    images = listOf(Image(height = 300, url = image2, width = 300)),
                    name = "Top Artist 2"
                )
            )
        )

        val topTracks = UserTopTracks(
            items = listOf(
                UserTopTracksItem(
                    album = Album(
                        artists = listOf(ArtistX(name = "Artist A")),
                        id = "album_a",
                        images = listOf(Image(300, image1, 300)),
                        name = "Album A",
                    ),
                    artists = listOf(ArtistX(name = "Artist A")),
                    name = "Top Track A",
                ),
                UserTopTracksItem(
                    album = Album(
                        artists = listOf(ArtistX(name = "Artist B")),
                        id = "album_b",
                        images = listOf(Image(300, image2, 300)),
                        name = "Album B",
                    ),
                    artists = listOf(ArtistX(name = "Artist B")),
                    name = "Top Track B",
                )
            )
        )

        val topAlbums = listOf(
            TopAlbum(
                album = Album(
                    artists = listOf(ArtistX(name = "Artist A")),
                    id = "album_a",
                    images = listOf(Image(300, image1, 300)),
                    name = "Album A",
                ),
                trackCount = 10
            ),
            TopAlbum(
                album = Album(
                    artists = listOf(ArtistX(name = "Artist B")),
                    id = "album_b",
                    images = listOf(Image(300, image2, 300)),
                    name = "Album B",
                ),
                trackCount = 12
            )
        )

        val previewState = MainUiState(
            isLoading = false,
            isRefreshing = false,
            errorMessage = null,
            userProfile = null,
            currentlyPlaying = nowPlaying,
            recentlyPlayed = recently,
            topArtists = topArtists,
            topTracks = topTracks,
            topAlbums = topAlbums,
            progressMs = nowPlaying.progress_ms
        )

        Surface(color = MaterialTheme.colorScheme.background) {
            MainContent(
                isRefreshing = mutableStateOf(false),
                refreshRecentlyPlayed = { Job() },
                paddingValues = PaddingValues(),
                onRecentlyShowClick = {},
                mainUiState = previewState
            )
        }
    }
}