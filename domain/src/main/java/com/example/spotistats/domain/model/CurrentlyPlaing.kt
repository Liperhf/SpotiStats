package com.example.spotistats.domain.model

data class CurrentlyPlaying(
    val is_playing: Boolean,
    val item: CurrentlyItem,
    val progress_ms: Int,
)