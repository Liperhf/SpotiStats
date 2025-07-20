package com.example.spotistats.data.dto

import android.content.Context
import com.example.spotistats.data.dto.common.AlbumDto
import com.example.spotistats.data.dto.common.ArtistXDto
import com.example.spotistats.data.dto.common.ContextDto
import com.example.spotistats.data.dto.common.CursorsDto
import com.example.spotistats.data.dto.common.ExternalIdsDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.ImageDto
import com.example.spotistats.data.dto.common.LinkedFromDto
import com.example.spotistats.data.dto.common.RecentlyItemDto
import com.example.spotistats.data.dto.common.RestrictionsXDto
import com.example.spotistats.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
data class RecentlyPlayedDto(
    val cursors: CursorsDto,
    val href: String,
    val items: List<RecentlyItemDto>,
    val limit: Int,
    val next: String,
    val total: Int
)

