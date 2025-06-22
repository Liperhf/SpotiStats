package com.example.spotistats.domain.model

import ActionsDto
import DeviceDto
import android.content.Context

data class CurrentlyPlaying(
    val is_playing: Boolean,
    val item: Item,
    val progress_ms: Int,
)