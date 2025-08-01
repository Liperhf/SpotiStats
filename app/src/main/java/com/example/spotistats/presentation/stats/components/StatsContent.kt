package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.presentation.stats.StatsViewModel

@Composable
fun StatsContent(
    paddingValues: PaddingValues,
    isLoading: State<Boolean?>,
    selectedContentType:State<StatsViewModel. ContentType>,
    topTracks:State<UserTopTracks?>,
    topArtists:State<UserTopArtists?>,
    topAlbums:State<List<TopAlbum?>>
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        if(isLoading.value == true) {
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