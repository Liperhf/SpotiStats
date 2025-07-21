package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.UserTopArtistDto
import com.example.spotistats.domain.model.UserTopArtists

fun UserTopArtistDto.toDomain():UserTopArtists{
    return UserTopArtists(
        items = this.items.map { it.toDomain() }
    )
}