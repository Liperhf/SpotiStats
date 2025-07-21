package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.AlbumDto
import com.example.spotistats.data.dto.common.ArtistXDto

data class UserTopTracksItem (
    val album: AlbumDto,
    val artists: List<ArtistXDto>,
    val name: String,
)