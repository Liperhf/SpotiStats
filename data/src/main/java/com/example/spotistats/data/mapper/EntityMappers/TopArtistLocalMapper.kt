package com.example.spotistats.data.mapper.EntityMappers

import com.example.spotistats.data.local.entity.TopArtistEntity
import com.example.spotistats.data.local.entity.TopTrackEntity
import com.example.spotistats.domain.model.Image
import com.example.spotistats.domain.model.UserTopArtistsItem

fun TopArtistEntity.toDomain():UserTopArtistsItem{
    return UserTopArtistsItem(
        name = name,
        images = listOfNotNull(imageUrl?.let { Image(url = it, width = 0, height = 0) })
    )
}

fun UserTopArtistsItem.toEntity(timeRange:String,positions:Int):TopArtistEntity {
    val imageUrlValue = images.firstOrNull()?.url ?: ""
    return TopArtistEntity(
        id = name + timeRange,
        name = name,
        imageUrl = imageUrlValue,
        timeRange = timeRange,
        position = positions,
    )
}