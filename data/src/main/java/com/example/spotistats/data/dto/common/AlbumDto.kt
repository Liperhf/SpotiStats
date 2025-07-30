package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val album_type: String,
    val artists: List<ArtistXDto>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: RestrictionsXDto,
    val total_tracks: Int,
    val type: String,
    val uri: String
)