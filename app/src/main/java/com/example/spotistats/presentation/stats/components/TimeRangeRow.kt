package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotistats.R
import com.example.spotistats.presentation.stats.StatsUiState
import com.example.spotistats.presentation.stats.StatsViewModel
import com.example.spotistats.presentation.stats.TimeRange

@Composable
fun TimeRangeRow(
    selectedTimeRange: TimeRange,
    onUpdateTimeRange:(TimeRange) -> Unit,
    uiState: StatsUiState
){
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
            TimeRange.values().forEach { range ->
                val isSelected = uiState.selectedTimeRange == range
                Button(onClick = {onUpdateTimeRange(range)},
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
                        TimeRange.SHORT -> {
                            Text(text = stringResource(R.string.last_months),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp)
                        }
                        TimeRange.MEDIUM -> {
                            Text(text = stringResource(R.string.last_six_months),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp)
                        }
                        TimeRange.LONG -> {
                            Text(text = stringResource(R.string.last_year),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
