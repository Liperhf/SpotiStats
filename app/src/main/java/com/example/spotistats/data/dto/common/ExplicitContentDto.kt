package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ExplicitContentDto(
    val filter_enabled: Boolean,
    val filter_locked: Boolean
)