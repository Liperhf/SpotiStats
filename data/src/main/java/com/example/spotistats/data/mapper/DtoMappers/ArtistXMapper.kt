package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.common.ArtistXDto
import com.example.spotistats.domain.model.ArtistX

fun ArtistXDto.toDomain(): ArtistX {
    return ArtistX(
        name = this.name,
    )
}