package com.example.spotistats.data.dto

import android.content.Context
import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.data.dto.common.ImageDto
import com.example.spotistats.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
data class RecentlyPlayedDto(
    val cursors: CursorsDto,
    val href: String,
    val items: List<ItemDto>,
    val limit: Int,
    val next: String,
    val total: Int
)

@Serializable
data class CursorsDto(
    val after: String,
    val before: String
)

@Serializable
data class ItemDto(
    val context: ContextDto,
    val played_at: String,
    val track: TrackDto
)

@Serializable
data class ContextDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val type: String,
    val uri: String
)

@Serializable
data class TrackDto(
    val album: AlbumDto,
    val artists: List<ArtistXDto>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIdsDto,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val linked_from: LinkedFromDto,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val restrictions: RestrictionsXDto,
    val track_number: Int,
    val type: String,
    val uri: String
)

@Serializable
data class AlbumDto(
    val album_type: String,
    val artists: List<ArtistXDto>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: RestrictionsXDto,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

@Serializable
data class ArtistXDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

@Serializable
data class ExternalIdsDto(
    val ean: String,
    val isrc: String,
    val upc: String
)

class LinkedFromDto

@Serializable
data class RestrictionsXDto(
    val reason: String
)
