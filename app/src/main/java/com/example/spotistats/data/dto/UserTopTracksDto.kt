package com.example.spotistats.data.dto

import com.example.spotistats.data.dto.common.UserTopTracksItemDto

data class UserTopTracksDto(
    val href: String,
    val items: List<UserTopTracksItemDto>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)