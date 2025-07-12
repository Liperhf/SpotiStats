package com.example.spotistats.data.mapper

import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.domain.model.RecentlyPlayed

fun RecentlyPlayedDto.toDomain():RecentlyPlayed{
    return RecentlyPlayed(
        tracks =    items.map{it.track.toDomain()},
        total = total
    )
}