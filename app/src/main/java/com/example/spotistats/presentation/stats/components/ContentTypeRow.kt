package com.example.spotistats.presentation.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.spotistats.R
import com.example.spotistats.presentation.stats.StatsViewModel

@Composable
fun ContentTypeRow(
    selectedContentType: State<StatsViewModel.ContentType>,
    onUpdateContentType:(StatsViewModel.ContentType) -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween){
        StatsViewModel.ContentType.values().forEach { type ->
            val isSelected = selectedContentType.value == type
            Button(onClick = { onUpdateContentType(type) },
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