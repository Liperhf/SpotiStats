package com.example.spotistats.data.mapper.DtoMappers

import com.example.spotistats.data.dto.common.ExternalUrlsDto
import com.example.spotistats.domain.model.ExternalUrls

fun ExternalUrlsDto.toDomain(): ExternalUrls {
    return ExternalUrls(
        spotify = this.spotify
    )
}