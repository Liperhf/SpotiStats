package com.example.spotistats.data.dto.common

data class ArtistXDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)