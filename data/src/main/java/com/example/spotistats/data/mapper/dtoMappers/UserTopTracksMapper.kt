package com.example.spotistats.data.mapper.dtoMappers

import com.example.spotistats.data.dto.UserTopTracksDto
import com.example.spotistats.domain.model.UserTopTracks

fun UserTopTracksDto.toDomain(): UserTopTracks {
    return UserTopTracks(
        items = this.items.map { it.toDomain() }
    )
}