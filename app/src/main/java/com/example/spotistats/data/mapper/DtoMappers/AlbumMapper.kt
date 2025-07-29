package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.common.AlbumDto
import com.example.spotistats.domain.model.Album

fun AlbumDto.toDomain():Album{
    return Album(
        artists = this.artists.map { it.toDomain() },
        id = this.id,
        images = this.images.map { it.toDomain() },
        name = this.name,
    )
}