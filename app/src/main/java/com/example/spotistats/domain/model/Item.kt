package com.example.spotistats.domain.model

data class Item(
    val artists: String,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val imageUrl:String
)