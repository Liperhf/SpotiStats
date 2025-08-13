package com.example.spotistats.data.dto

import com.example.spotistats.data.dto.common.ExplicitContentDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.FollowersDto
import com.example.spotistats.data.dto.common.ImageDto

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

