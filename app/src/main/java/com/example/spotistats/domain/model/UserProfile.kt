package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.ExplicitContentDto
import com.example.spotistats.data.dto.FollowersDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.ImageDto

data class UserProfile(
    val display_name: String,
    val imagesUrl: String,
)