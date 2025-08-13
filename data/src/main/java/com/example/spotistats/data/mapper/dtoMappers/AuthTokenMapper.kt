package com.example.spotistats.data.mapper.dtoMappers

import com.example.spotistats.data.dto.AuthTokenDto
import com.example.spotistats.domain.model.AuthToken

fun AuthTokenDto.toDomain(): AuthToken {
    return AuthToken(
        access_token = this.access_token,
        token_type = this.token_type,
        expires_in = this.expires_in,
        refresh_token = this.refresh_token,
        scope = this.scope
    )
}