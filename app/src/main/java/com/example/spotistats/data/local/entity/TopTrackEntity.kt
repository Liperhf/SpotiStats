package com.example.spotistats.data.local.entity

import android.icu.text.Transliterator.Position
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_tracks")
data class TopTrackEntity(
    @PrimaryKey val id:String,
    val name:String,
    val artists:String,
    val position:Int,
    val timeRange:String,
    val albumId: String,
    val albumName: String,
    val albumImageUrl: String?,
    val albumArtists:String
)