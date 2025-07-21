package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.UserTopTracksItemDto

data class UserTopTracks (
    val items: List<UserTopTracksItem>,
)