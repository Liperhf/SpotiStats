package com.example.spotistats.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun NowPlayingBox(
    imageUrl: String,
    name: String,
    artist: String,
    durationMs: Int,
    progressMs: Int
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .height(160.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.now_playing),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                AsyncImage(
                    model = R.drawable.spotify,
                    contentDescription = stringResource(R.string.spotify),
                    modifier = Modifier
                        .size(30.dp)
                )
            }


            Row {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.track_image),
                    modifier = Modifier
                        .size(105.dp, 105.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                    Text(
                        name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        artist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    TrackProgressBar(progressMs, durationMs)
                }
            }
        }
    }
}

@Preview(name = "NowPlayingBox", showBackground = true)
@Composable
private fun NowPlayingBoxPreview() {
    SpotiStatsTheme {
        NowPlayingBox(
            imageUrl = "",
            name = "Track Name",
            artist = "Artist Name",
            durationMs = 210_000,
            progressMs = 45_000
        )
    }
}