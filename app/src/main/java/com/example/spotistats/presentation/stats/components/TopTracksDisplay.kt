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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.UserTopTracksItem

@Composable
fun TopTracksDisplay(tracks:List<UserTopTracksItem>){
    LazyColumn {
        if (tracks != null) {
            items(tracks.size){
                val track = tracks[it]
                val artists = track.artists.joinToString(","){it.name}
                val name = track.name
                val imageUrl = track.album.images.firstOrNull()?.url
                Row(modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 7.dp)
                    .fillMaxWidth()){
                    val number:Int = it + 1
                    Text(text = number.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .width(32.dp)
                            .padding(end = 9.dp))
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

                        if (artists != null) {
                            Text(text = artists,
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
}