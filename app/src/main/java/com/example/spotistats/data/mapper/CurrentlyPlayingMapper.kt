package com.example.spotistats.data.mapper

import CurrentlyPlayingDto
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.data.mapper.*

fun CurrentlyPlayingDto.toDomain():CurrentlyPlaying{
    return CurrentlyPlaying(
        is_playing = this.is_playing,
        item = this.item.toDomain(),
        progress_ms = this.progress_ms
    )
}