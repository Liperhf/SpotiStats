package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.common.UserTopItemDto
import com.example.spotistats.domain.model.UserTopArtistsItem

fun UserTopItemDto.toDomain():UserTopArtistsItem{
    return UserTopArtistsItem(
        images = this.images,
        name = this.name
    )
}