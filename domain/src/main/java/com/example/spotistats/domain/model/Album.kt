package com.example.spotistats.domain.model

data class Album(
    val artists: List<ArtistX>,
    val id: String,
    val images: List<Image>,
    val name: String,

)