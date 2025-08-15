package com.example.spotistats.presentation.stats

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotistats.presentation.stats.components.ContentTypeRow
import com.example.spotistats.presentation.stats.components.StatsContent
import com.example.spotistats.presentation.stats.components.TimeRangeRow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStats()
    }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            ContentTypeRow(
                onUpdateContentType = viewModel::updateContentType,
                uiState = uiState.value
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(paddingValues)
        ) {
            TimeRangeRow(
                selectedTimeRange = uiState.value.selectedTimeRange,
                onUpdateTimeRange = viewModel::updateTimeRange,
                uiState = uiState.value
            )
            StatsContent(
                paddingValues = androidx.compose.foundation.layout.PaddingValues(),
                uiState = uiState.value
            )
        }
    }

}







