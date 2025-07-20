package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ArtistXDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)