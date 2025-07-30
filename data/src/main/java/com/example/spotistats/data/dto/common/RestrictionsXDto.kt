package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class RestrictionsXDto(
    val reason: String
)