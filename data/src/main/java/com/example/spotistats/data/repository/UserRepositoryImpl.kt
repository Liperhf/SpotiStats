package com.example.spotistats.data.repository

import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.mapper.dtoMappers.toDomain
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val spotifyUserApi: SpotifyUserApi,
    private val authRepository: AuthRepository,
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val token =
            authRepository.getAccessToken() ?: throw IllegalStateException("No access token")
        val dto = spotifyUserApi.getUserProfile("Bearer $token")
        return dto.toDomain()
    }
}