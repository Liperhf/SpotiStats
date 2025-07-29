package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.AlbumDto
import com.example.spotistats.data.dto.common.ArtistXDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto

data class UserTopTracksItem (
    val album: Album,
    val artists: List<ArtistX>,
    val name: String,
)