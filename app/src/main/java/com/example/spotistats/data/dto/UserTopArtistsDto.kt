package com.example.spotistats.data.dto

import com.example.spotistats.data.dto.common.UserTopArtistsItemDto

data class UserTopArtistsDto(
    val href: String,
    val items: List<UserTopArtistsItemDto>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)