package com.example.spotistats.domain.model

import com.example.spotistats.data.dto.common.ArtistXDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.ImageDto
import com.example.spotistats.data.dto.common.RestrictionsXDto

data class Album (
    val album_type: String,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)