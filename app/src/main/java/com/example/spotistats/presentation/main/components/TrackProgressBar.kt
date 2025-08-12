package com.example.spotistats.presentation.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotistats.ui.theme.SpotiStatsTheme
import com.example.spotistats.util.formatTime

@Composable
fun TrackProgressBar(
    progressMs: Int,
    durationMs: Int
) {
    val progress = if (durationMs > 0) progressMs.toFloat() / durationMs else 0f

    Column {
        Row() {
            Text(
                text = formatTime(progressMs),
                modifier = Modifier
                    .padding(top = 4.dp),
                fontSize = 14.sp
            )
            Text(
                text = formatTime(durationMs),
                modifier = Modifier.padding(start = 170.dp, top = 4.dp),
                fontSize = 14.sp
            )
        }
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
private fun TrackProgressPreview() {
    SpotiStatsTheme {
        TrackProgressBar(
            progressMs = 45_000,
            durationMs = 210_000
        )
    }
}