package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.common.UserTopArtistsItemDto
import com.example.spotistats.domain.model.UserTopArtistsItem

fun UserTopArtistsItemDto.toDomain():UserTopArtistsItem{
    return UserTopArtistsItem(
        images = this.images.map { it.toDomain() },
        name = this.name,
    )
}