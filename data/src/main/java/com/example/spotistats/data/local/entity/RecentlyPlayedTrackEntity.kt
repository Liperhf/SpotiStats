package com.example.spotistats.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recently_played")
data class RecentlyPlayedTrackEntity(
    @PrimaryKey val id: String,
    val name: String,
    val artists: String,
    val album: String,
    val durationMs: Int,
    val type: String,
    val uri: String,
    val imageUrl: String?,
    val position: Int,
)
