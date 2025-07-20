package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.common.TrackDto
import com.example.spotistats.domain.model.Track

fun TrackDto.toDomain() : Track{
    return Track(
        id = this.id,
        album = this.album.name,
        artists = this.artists.firstOrNull()?.name ?: "Unknown",
        duration_ms = this.duration_ms,
        name = this.name,
        type = this.type,
        uri = this.uri,
        imageUrl = this.album.images.firstOrNull()?.url ?: "",
    )
}