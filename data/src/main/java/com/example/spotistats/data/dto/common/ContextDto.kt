package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ContextDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val type: String,
    val uri: String
)