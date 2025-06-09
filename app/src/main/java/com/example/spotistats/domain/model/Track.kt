package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.Album
import com.example.spotistats.data.dto.ArtistX
import com.example.spotistats.data.dto.ExternalIds
import com.example.spotistats.data.dto.ExternalUrlsXXX
import com.example.spotistats.data.dto.LinkedFrom
import com.example.spotistats.data.dto.RestrictionsX

data class Track (
    val artists: List<ArtistX>,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val popularity: Int,
    val type: String,
    val uri: String
)