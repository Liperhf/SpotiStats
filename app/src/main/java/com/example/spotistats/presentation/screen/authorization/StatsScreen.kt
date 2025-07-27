package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtistsItem
import com.example.spotistats.domain.model.UserTopTracksItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController,
                viewModel:StatsViewModel){

    val topTracks = viewModel.topTracks.collectAsState()
    val topArtists = viewModel.topArtists.collectAsState()
    val topAlbums = viewModel.topAlbums.collectAsState()
    val selectedContentType = viewModel.selectedContentType.collectAsState()
    val selectedTimeRange = viewModel.selectedTimeRange.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    Scaffold(
        topBar = {
            Column() {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                    horizontalArrangement  = Arrangement.Center,)
                { Text(stringResource(R.string.top),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)}
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    StatsViewModel.TimeRange.values().forEach { range ->
                        val isSelected = selectedTimeRange.value == range
                        Button(onClick = {viewModel.updateTimeRange(range)},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = if(isSelected){
                                    MaterialTheme.colorScheme.onBackground
                                }else{
                                    MaterialTheme.colorScheme.onSurface
                                },
                            )
                            )
                        {
                            when(range){
                                StatsViewModel.TimeRange.SHORT -> {
                                    Text(text = stringResource(R.string.last_months),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp)
                                }
                                StatsViewModel.TimeRange.MEDIUM -> {
                                    Text(text = stringResource(R.string.last_six_months),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp)
                                }
                                StatsViewModel.TimeRange.LONG -> {
                                    Text(text = stringResource(R.string.last_year),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
            StatsViewModel.ContentType.values().forEach { type ->
                val isSelected = selectedContentType.value == type
                Button(onClick = { viewModel.updateContentType(type) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = if(isSelected){
                            MaterialTheme.colorScheme.onBackground
                        }else{
                            MaterialTheme.colorScheme.onSurface
                        },
                    ),
                ) {
                        when(type){
                            StatsViewModel.ContentType.TRACKS -> {
                                Text(text = stringResource(R.string.tracks),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp)
                            }
                            StatsViewModel.ContentType.ARTISTS -> {
                                Text(text = stringResource(R.string.artists),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp)
                            }
                            StatsViewModel.ContentType.ALBUMS -> {
                                Text(text = stringResource(R.string.albums),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    ) {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if(isLoading.value == false) {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground)
            }else{
                when(selectedContentType.value){
                    StatsViewModel.ContentType.TRACKS -> {
                        topTracks.value?.items.let { tracks ->
                            if (tracks != null) {
                                TopTracksDisplay(tracks)
                            }
                        }
                    }
                    StatsViewModel.ContentType.ARTISTS -> {
                        topArtists.value?.items.let { artists ->
                            if(artists != null){
                                TopArtistsDisplay(artists)
                            }

                        }
                    }

                    StatsViewModel.ContentType.ALBUMS -> {
                        topAlbums.value.let { albums ->
                            if(albums != null){
                                TopAlbumsDisplay(albums)
                            }
                        }
                    }
                }
            }
        }
    }

}



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

@Composable
fun TopArtistsDisplay(artists:List<UserTopArtistsItem>){
    LazyColumn {
        if (artists != null) {
            items(artists.size){
                val artist = artists[it]
                val name = artist.name
                val imageUrl = artist.images.firstOrNull()?.url
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
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.place_holder_artist),
                        error = painterResource(R.drawable.place_holder_artist)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column() {
                        Text(text = name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1)
                    }
                }
            }
        }
    }
}



@Composable
fun TopAlbumsDisplay(albums: List<TopAlbum?>){
    LazyColumn {
        if (albums != null) {
            items(albums.size){
                val album = albums[it]
                val name = album?.album?.name
                val imageUrl = album?.album?.images?.firstOrNull()?.url
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
                        if (name != null) {
                            Text(text = name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}