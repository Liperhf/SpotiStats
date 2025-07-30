package com.example.spotistats.data.mapper.EntityMappers

import com.example.spotistats.data.local.dao.TopTracksDao
import com.example.spotistats.data.local.entity.TopTrackEntity
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.ArtistX
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.UserTopTracksItem

fun TopTrackEntity.toDomain():UserTopTracksItem{
    return UserTopTracksItem(
        album = Album(
            images = listOfNotNull(albumImageUrl?.let { Image(
                url = it,
                height = 0,
                width = 0
            ) }),
            name = albumName,
            id = albumId,
            artists = albumArtists.split(",").map { ArtistX(name = it.trim()) },
        ),
        artists = artists.split(",").map { ArtistX(name = it.trim()) },
        name = name,
    )
}

fun UserTopTracksItem.toEntity(timeRange:String,position:Int):TopTrackEntity {
        return TopTrackEntity(
            id = name + timeRange,
            name = name,
            artists = this.artists.joinToString(",") { it.name },
            position = position,
            timeRange = timeRange,
            albumId = album.id,
            albumName = album.name,
            albumImageUrl = album.images.firstOrNull()?.url,
            albumArtists = album.artists.joinToString(","){it.name}
        )
}
