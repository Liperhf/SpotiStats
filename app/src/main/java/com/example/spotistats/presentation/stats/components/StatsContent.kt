package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.model.UserTopTracksItem
import com.example.spotistats.presentation.stats.ContentType
import com.example.spotistats.presentation.stats.StatsUiState
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun StatsContent(
    paddingValues: PaddingValues,
    uiState: StatsUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (uiState.isLoading == true) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else {
            when (uiState.selectedContentType) {
                ContentType.TRACKS -> {
                    uiState.topTracks?.items.let { tracks ->
                        if (tracks != null) {
                            TopTracksDisplay(tracks)
                        }
                    }
                }

                ContentType.ARTISTS -> {
                    uiState.topArtists?.items.let { artists ->
                        if (artists != null) {
                            TopArtistsDisplay(artists)
                        }

                    }
                }

                ContentType.ALBUMS -> {
                    TopAlbumsDisplay(uiState.topAlbums)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun StatsContentPreview() {
    SpotiStatsTheme {
        val image1 = "https://picsum.photos/seed/stats1/300/300"
        val image2 = "https://picsum.photos/seed/stats2/300/300"

        val mockTopTracks = UserTopTracks(
            items = listOf(
                UserTopTracksItem(
                    name = "Top Track 1",
                    album = Album(
                        id = "alb1",
                        name = "Album 1",
                        artists = listOf(ArtistX(name = "Artist 1")),
                        images = listOf(Image(300, image1, 300))
                    ),
                    artists = listOf(ArtistX(name = "Artist 1"))
                ),
                UserTopTracksItem(
                    name = "Top Track 2",
                    album = Album(
                        id = "alb2",
                        name = "Album 2",
                        artists = listOf(ArtistX(name = "Artist 2")),
                        images = listOf(Image(300, image2, 300))
                    ),
                    artists = listOf(ArtistX(name = "Artist 2"))
                )
            )
        )

        val mockTopArtists = UserTopArtists(
            items = listOf(
                UserTopArtistsItem(
                    name = "Top Artist 1",
                    images = listOf(Image(300, image1, 300))
                ),
                UserTopArtistsItem(
                    name = "Top Artist 2",
                    images = listOf(Image(300, image2, 300))
                )
            )
        )

        val mockTopAlbums: List<TopAlbum?> = listOf(
            TopAlbum(
                album = Album(
                    id = "alb1",
                    name = "Album 1",
                    artists = listOf(ArtistX(name = "Artist 1")),
                    images = listOf(Image(300, image1, 300))
                ),
                trackCount = 10
            ),
            TopAlbum(
                album = Album(
                    id = "alb2",
                    name = "Album 2",
                    artists = listOf(ArtistX(name = "Artist 2")),
                    images = listOf(Image(300, image2, 300))
                ),
                trackCount = 12
            )
        )

        val state = StatsUiState(
            selectedContentType = ContentType.TRACKS,
            topTracks = mockTopTracks,
            topArtists = mockTopArtists,
            topAlbums = mockTopAlbums,
            isLoading = false
        )

        Surface(color = MaterialTheme.colorScheme.background) {
            StatsContent(
                paddingValues = PaddingValues(),
                uiState = state
            )
        }
    }
}