package com.example.spotistats.presentation.stats

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.presentation.stats.components.ContentTypeRow
import com.example.spotistats.presentation.stats.components.StatsContent
import com.example.spotistats.presentation.stats.components.TimeRangeRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(navController: NavController,
                viewModel: StatsViewModel
){
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    Scaffold(
        topBar = {
            TimeRangeRow(
                selectedTimeRange = uiState.value.selectedTimeRange,
                onUpdateTimeRange = viewModel::updateTimeRange,
                uiState = uiState.value
            )
        },
        bottomBar = {
            ContentTypeRow(
                onUpdateContentType = viewModel::updateContentType,
                uiState = uiState.value
            )
        }
    ) {paddingValues ->
            StatsContent(
                paddingValues = paddingValues,
                uiState = uiState.value
            )

    }

}







