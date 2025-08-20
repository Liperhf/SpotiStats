package com.example.spotistats.domain.useCases

import android.content.Intent
import com.example.spotistats.domain.model.AuthToken
import com.example.spotistats.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun createAuthIntent(): Intent {
        return authRepository.buildAuthIntent()
    }

    suspend fun exchangeCodeForToken(code: String): AuthToken {
        return authRepository.exchangeCodeForToken(code)
    }

    suspend fun saveTokens(authTokenDto: AuthToken) {
        return authRepository.saveTokens(authTokenDto)
    }

    suspend fun getAccessToken(): String? {
        return authRepository.getAccessToken()
    }

    suspend fun getRefreshToken(): String? {
        return authRepository.getRefreshToken()
    }

    suspend fun refreshTokenIfNeeded(): Boolean {
        return try {
            if (isTokenExpired()) {
                val refreshToken = getRefreshToken()
                if (refreshToken != null) {
                    val newAuthDto = authRepository.refreshToken(refreshToken)
                    saveTokens(newAuthDto)
                    true
                } else false
            } else true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun isUserAuthorized(): Boolean {
        getAccessToken() ?: return false
        if (isTokenExpired()) {
            return refreshTokenIfNeeded()
        }
        return true
    }

    suspend fun clearTokens() {
        return authRepository.clearTokens()
    }

    suspend fun isTokenExpired(): Boolean {
        return authRepository.isTokenExpired()
    }

}