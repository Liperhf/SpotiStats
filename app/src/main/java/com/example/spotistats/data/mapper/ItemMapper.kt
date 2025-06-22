package com.example.spotistats.data.mapper

import CurrentlyItemDto
import com.example.spotistats.data.dto.ItemDto
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.Item

fun CurrentlyItemDto.toDomain():Item{
    return Item(
        artists = artists.joinToString(", ") { it.name },
        duration_ms = this.duration_ms,
        id = this.id,
        name = this.name,
        imageUrl = this.album.images.firstOrNull()?.url ?: ""
    )
}