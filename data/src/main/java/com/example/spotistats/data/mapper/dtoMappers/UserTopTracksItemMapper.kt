package com.example.spotistats.data.mapper.dtoMappers

import com.example.spotistats.data.dto.common.UserTopTracksItemDto
import com.example.spotistats.domain.model.UserTopTracksItem

fun UserTopTracksItemDto.toDomain(): UserTopTracksItem {
    return UserTopTracksItem(
        album = this.album.toDomain(),
        artists = artists.map { it.toDomain() },
        name = this.name,
    )
}