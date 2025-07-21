package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.common.UserTopTracksItemDto
import com.example.spotistats.domain.model.UserTopTracksItem

fun UserTopTracksItemDto.toDomain():UserTopTracksItem{
    return UserTopTracksItem(
        album = this.album,
        artists = this.artists,
        name = this.name
    )
}