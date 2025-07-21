package com.example.spotistats.data.mapper

import androidx.compose.ui.Modifier
import com.example.spotistats.data.dto.common.ImageDto
import com.example.spotistats.domain.model.Image

fun ImageDto.toDomain():Image{
    return Image(
        height = this.height,
        url = this.url,
        width = this.width
    )
}