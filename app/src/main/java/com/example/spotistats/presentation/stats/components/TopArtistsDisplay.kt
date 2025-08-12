package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
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
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun TopArtistsDisplay(artists: List<UserTopArtistsItem>) {
    LazyColumn {
        items(artists.size) {
            val artist = artists[it]
            val name = artist.name
            val imageUrl = artist.images.firstOrNull()?.url
            Row(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 7.dp)
                    .fillMaxWidth()
            ) {
                val number: Int = it + 1
                Text(
                    text = number.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .width(32.dp)
                        .padding(end = 9.dp)
                )
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.listened_recently),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.place_holder_artist),
                    error = painterResource(R.drawable.place_holder_artist)
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
                }
            }
        }
    }
}

@Preview
@Composable
private fun TopArtistsDisplayPreview() {
    SpotiStatsTheme {
        TopArtistsDisplay(artists = List(10) {
            UserTopArtistsItem(
                name = "Artist $it",
                images = listOf(
                    Image(
                        height = 640,
                        url = "https://album.cover.url",
                        width = 640
                    )
                )
            )
        })
    }
}