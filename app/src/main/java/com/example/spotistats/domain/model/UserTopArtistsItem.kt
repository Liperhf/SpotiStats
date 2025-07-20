package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.ImageDto

data class UserTopArtistsItem (
    val images: List<ImageDto>,
    val name: String,
)