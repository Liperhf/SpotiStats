package com.example.spotistats.domain.repository

import com.example.spotistats.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
}