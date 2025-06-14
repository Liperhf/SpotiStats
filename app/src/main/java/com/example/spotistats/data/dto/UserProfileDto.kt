package com.example.spotistats.data.dto

import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.ImageDto
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val country: String,
    val display_name: String,
    val email: String,
    val explicit_content: ExplicitContentDto,
    val external_urls: ExternalUrlsDto,
    val followers: FollowersDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val product: String,
    val type: String,
    val uri: String
)

@Serializable
data class ExplicitContentDto(
    val filter_enabled: Boolean,
    val filter_locked: Boolean
)

@Serializable
data class FollowersDto(
    val href: String,
    val total: Int
)
