package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.common.CurrentlyItemDto
import com.example.spotistats.domain.model.CurrentlyItem

fun CurrentlyItemDto.toDomain():CurrentlyItem{
    return CurrentlyItem(
        artists = artists.joinToString(", ") { it.name },
        duration_ms = this.duration_ms,
        id = this.id,
        name = this.name,
        imageUrl = this.album.images.firstOrNull()?.url ?: ""
    )
}