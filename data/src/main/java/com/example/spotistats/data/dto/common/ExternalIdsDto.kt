package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ExternalIdsDto(
    val ean: String,
    val isrc: String,
    val upc: String
)