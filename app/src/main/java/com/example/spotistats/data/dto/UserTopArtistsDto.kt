package com.example.spotistats.data.dto

import com.example.spotistats.data.dto.common.UserTopItemDto

data class UserTopArtistsDto(
    val href: String,
    val items: List<UserTopItemDto>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)