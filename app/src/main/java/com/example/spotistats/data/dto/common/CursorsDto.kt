package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class CursorsDto(
    val after: String,
    val before: String
)