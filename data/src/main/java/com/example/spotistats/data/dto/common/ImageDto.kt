package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ImageDto(
    val height: Int,
    val url: String,
    val width: Int
)