package com.example.spotistats.domain.model


data class Track(
    val album: String,
    val artists: String,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val type: String,
    val uri: String,
    val imageUrl:String
)