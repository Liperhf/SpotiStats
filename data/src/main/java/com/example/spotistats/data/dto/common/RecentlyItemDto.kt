package com.example.spotistats.data.dto.common

data class RecentlyItemDto(
    val context: ContextDto,
    val played_at: String,
    val track: TrackDto
)