package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.common.AlbumDto
import com.example.spotistats.domain.model.Album

fun AlbumDto.toDomain():Album{
    return Album(
        album_type = this.album_type,
        artists = this.artists.map { it.toDomain() },
        available_markets = this.available_markets,
        href = this.href,
        id = this.id,
        images = this.images.map { it.toDomain() },
        name = this.name,
        release_date = this.release_date,
        release_date_precision = this.release_date_precision,
        total_tracks = this.total_tracks,
        type = this.type,
        uri = this.uri
    )
}