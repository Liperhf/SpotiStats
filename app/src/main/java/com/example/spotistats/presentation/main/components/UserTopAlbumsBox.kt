package com.example.spotistats.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun UserTopAlbumsBox(userTopAlbums: List<TopAlbum?>) {
    Box(
        modifier = Modifier
            .padding(top = 30.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .height(250.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.top_albums_month),
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
            LazyRow() {
                items(userTopAlbums.size) {
                    val album = userTopAlbums[it]?.album
                    val artist = album?.artists?.firstOrNull()?.name
                    val imageUrl = album?.images?.firstOrNull()?.url
                    val title = album?.name
                    Column(
                        modifier = Modifier
                            .width(150.dp)
                            .padding(7.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = stringResource(R.string.track_image),
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.place_holder_track)
                        )
                        if (title != null) {
                            Text(
                                title, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (artist != null) {
                            Text(
                                text = artist,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserTopAlbumsPreview() {
    SpotiStatsTheme {
        List(10) {
            TopAlbum(
                album = Album(
                    id = "album_id",
                    name = "Fake Album",
                    artists = listOf(ArtistX(name = "Fake Album")),
                    images = listOf(
                        Image(
                            height = 640,
                            url = "https://album.cover.url",
                            width = 640
                        )
                    )
                ),
                trackCount = it + 1
            )
        }
    }
}