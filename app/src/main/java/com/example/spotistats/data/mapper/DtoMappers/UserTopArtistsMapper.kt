package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.UserTopArtistsDto
import com.example.spotistats.domain.model.UserTopArtists

fun UserTopArtistsDto.toDomain():UserTopArtists{
    return UserTopArtists(
        items = this.items.map { it.toDomain() }
    )
}