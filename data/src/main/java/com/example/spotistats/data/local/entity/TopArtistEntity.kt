package com.example.spotistats.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_artist")
data class TopArtistEntity(
    @PrimaryKey val id:String,
    val name:String,
    val imageUrl:String,
    val timeRange:String,
    val position:Int,
)