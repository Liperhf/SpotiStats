package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.common.ArtistXDto
import com.example.spotistats.domain.model.ArtistX

fun ArtistXDto.toDomain():ArtistX{
    return ArtistX(
        href = this.href,
        id = this.id,
        name = this.name,
        type = this.type,
        uri = this.uri
    )
}