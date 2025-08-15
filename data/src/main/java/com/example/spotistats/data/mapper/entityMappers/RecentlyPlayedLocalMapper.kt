package com.example.spotistats.data.mapper.entityMappers

import com.example.spotistats.data.local.entity.RecentlyPlayedTrackEntity
import com.example.spotistats.domain.model.Track

fun Track.toEntity(position: Int): RecentlyPlayedTrackEntity {
    return RecentlyPlayedTrackEntity(
        id = id,
        name = name,
        artists = artists,
        album = album,
        durationMs = duration_ms,
        type = type,
        uri = uri,
        imageUrl = imageUrl,
        position = position
    )
}

fun RecentlyPlayedTrackEntity.toDomain(): Track {
    return Track(
        album = album,
        artists = artists,
        duration_ms = durationMs,
        id = id,
        name = name,
        type = type,
        uri = uri,
        imageUrl = imageUrl ?: ""
    )
}
