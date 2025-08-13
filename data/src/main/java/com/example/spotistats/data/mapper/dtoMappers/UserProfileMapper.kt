package com.example.spotistats.data.mapper.dtoMappers

import com.example.spotistats.data.dto.UserProfileDto
import com.example.spotistats.domain.model.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        imagesUrl = this.images.firstOrNull()?.url ?: "",
        display_name = this.display_name
    )
}