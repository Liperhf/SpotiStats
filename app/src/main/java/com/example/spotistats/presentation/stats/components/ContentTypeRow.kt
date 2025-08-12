package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.spotistats.R
import com.example.spotistats.presentation.stats.ContentType
import com.example.spotistats.presentation.stats.StatsUiState
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun ContentTypeRow(
    onUpdateContentType: (ContentType) -> Unit,
    uiState: StatsUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ContentType.entries.forEach { type ->
            val isSelected = uiState.selectedContentType == type
            Button(
                onClick = { onUpdateContentType(type) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = if (isSelected) {
                        MaterialTheme.colorScheme.onBackground
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                ),
            ) {
                when (type) {
                    ContentType.TRACKS -> {
                        Text(
                            text = stringResource(R.string.tracks),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    ContentType.ARTISTS -> {
                        Text(
                            text = stringResource(R.string.artists),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    ContentType.ALBUMS -> {
                        Text(
                            text = stringResource(R.string.albums),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ContentTypeRowPreview() {
    SpotiStatsTheme {
        ContentTypeRow(onUpdateContentType = {}, uiState = StatsUiState())
    }
}