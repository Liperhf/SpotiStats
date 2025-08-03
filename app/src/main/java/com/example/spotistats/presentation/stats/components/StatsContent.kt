package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.presentation.stats.ContentType
import com.example.spotistats.presentation.stats.StatsUiState

@Composable
fun StatsContent(
    paddingValues: PaddingValues,
    uiState: StatsUiState
){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {
        if(uiState.isLoading == true) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }else{
            when(uiState.selectedContentType){
                ContentType.TRACKS -> {
                    uiState.topTracks?.items.let { tracks ->
                        if (tracks != null) {
                            TopTracksDisplay(tracks)
                        }
                    }
                }
                ContentType.ARTISTS -> {
                    uiState.topArtists?.items.let { artists ->
                        if(artists != null){
                            TopArtistsDisplay(artists)
                        }

                    }
                }

                ContentType.ALBUMS -> {
                    uiState.topAlbums?.let { albums ->
                        if(albums != null){
                            TopAlbumsDisplay(albums)
                        }
                    }
                }
            }
        }
    }
}