package com.example.spotistats.presentation.screen.authorization

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(
    viewModel: AuthViewModel
) {
    val recentlyPlayed = viewModel.recentlyPlayed.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecentlyPlayed()
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        recentlyPlayed.value?.tracks?.let { tracks ->
            items(tracks) { track ->
                Text(track.name)
            }
        }
    }
}