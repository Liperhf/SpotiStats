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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.Track

@Composable
fun RecentlyContent(
    paddingValues: PaddingValues,
    recentlyPlayedTracks: List<Track>?
){
    LazyColumn(modifier = Modifier.padding(paddingValues)){
        if (recentlyPlayedTracks != null) {
            items(recentlyPlayedTracks.size){
                val track = recentlyPlayedTracks[it]
                val imageUrl = track.imageUrl
                val name = track.name
                val artist = track.artists
                Row(modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 7.dp)
                    .fillMaxWidth()){
                    AsyncImage(model = imageUrl,
                        contentDescription = stringResource(R.string.listened_recently),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        placeholder = painterResource(R.drawable.place_holder_track),
                        error = painterResource(R.drawable.place_holder_track)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column() {
                        Text(text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1)

                        Text(text = artist,
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