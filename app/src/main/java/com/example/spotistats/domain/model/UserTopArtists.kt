package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.UserTopItemDto

data class UserTopArtists(
    val items: List<UserTopArtistsItem>
)