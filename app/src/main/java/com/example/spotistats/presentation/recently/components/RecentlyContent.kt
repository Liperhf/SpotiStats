package com.example.spotistats.presentation.recently.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.Track
import com.example.spotistats.presentation.main.MainUiState
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun RecentlyContent(
    paddingValues: PaddingValues,
    uiState: MainUiState
) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        if (uiState.recentlyPlayed != null) {
            items(uiState.recentlyPlayed.tracks.size) {
                val track = uiState.recentlyPlayed.tracks[it]
                val imageUrl = track.imageUrl
                val name = track.name
                val artist = track.artists
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 7.dp)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.listened_recently),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        placeholder = painterResource(R.drawable.place_holder_track),
                        error = painterResource(R.drawable.place_holder_track)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column() {
                        Text(
                            text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        Text(
                            text = artist,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecentlyContentPreview() {
    SpotiStatsTheme {
        val image2 = "https://picsum.photos/seed/album1/300/300"
        val image3 = "https://picsum.photos/seed/album2/300/300"
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
        RecentlyContent(
            paddingValues = PaddingValues(0.dp),
            uiState = MainUiState(
                isLoading = false,
                isRefreshing = false,
                errorMessage = null,
                userProfile = null,
                recentlyPlayed = recently,
            )
        )
    }
}