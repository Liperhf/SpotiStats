package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class RecentlyItemDto(
    val context: ContextDto,
    val played_at: String,
    val track: TrackDto
)