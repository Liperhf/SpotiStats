package com.example.spotistats.data.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class UserTopItemDto(
    val external_urls: ExternalUrlsDto,
    val followers: FollowersDto,
    val genres: List<String>,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)