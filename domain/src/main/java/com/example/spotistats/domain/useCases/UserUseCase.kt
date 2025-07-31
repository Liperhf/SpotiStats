package com.example.spotistats.domain.useCases

import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.repository.UserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend fun getUserProfile(): UserProfile {
        return userRepository.getUserProfile()
    }
}