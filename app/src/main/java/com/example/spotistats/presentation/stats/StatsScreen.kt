package com.example.spotistats.presentation.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotistats.R
import com.example.spotistats.presentation.stats.components.ContentTypeRow
import com.example.spotistats.presentation.stats.components.StatsContent
import com.example.spotistats.presentation.stats.components.TimeRangeRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController,
                viewModel: StatsViewModel
){

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
            TimeRangeRow(viewModel = viewModel,
                selectedTimeRange = selectedTimeRange)
        },
        bottomBar = {
            ContentTypeRow(viewModel = viewModel,
                selectedContentType = selectedContentType,)
        }
    ) {paddingValues ->
            StatsContent(
                paddingValues = paddingValues,
                isLoading = isLoading,
                selectedContentType = selectedContentType,
                topTracks = topTracks,
                topArtists = topArtists,
                topAlbums = topAlbums)
                }

}







