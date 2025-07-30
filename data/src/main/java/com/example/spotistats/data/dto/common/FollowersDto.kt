package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class FollowersDto(
    val href: String,
    val total: Int
)