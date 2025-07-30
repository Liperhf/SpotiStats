package com.example.spotistats.domain.model


data class UserTopTracksItem (
    val album: Album,
    val artists: List<ArtistX>,
    val name: String,
)