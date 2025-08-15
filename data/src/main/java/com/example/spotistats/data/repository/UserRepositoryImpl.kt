package com.example.spotistats.data.repository

import android.content.Context
import com.example.spotistats.data.api.SpotifyUserApi
import com.example.spotistats.data.mapper.dtoMappers.toDomain
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.repository.AuthRepository
import com.example.spotistats.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val spotifyUserApi: SpotifyUserApi,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context,
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val token =
            authRepository.getAccessToken() ?: throw IllegalStateException("No access token")
        return try {
            val dto = spotifyUserApi.getUserProfile("Bearer $token")
            val profile = dto.toDomain()

            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val savedNickname = prefs.getString("nickname", null)
            val savedAvatar = prefs.getString("avatar", null)

            if (savedNickname.isNullOrEmpty() && savedAvatar.isNullOrEmpty()) {
                saveCachedProfile(profile)
            }

            UserProfile(
                display_name = savedNickname ?: profile.display_name,
                imagesUrl = savedAvatar ?: profile.imagesUrl
            )
        } catch (e: Exception) {
            getCachedProfile() ?: throw e
        }
    }

    private fun saveCachedProfile(profile: UserProfile) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val currentNick = prefs.getString("nickname", null)
        val currentAvatar = prefs.getString("avatar", null)
        if (currentNick.isNullOrEmpty()) {
            prefs.edit().putString("nickname", profile.display_name).apply()
        }
        if (currentAvatar.isNullOrEmpty()) {
            prefs.edit().putString("avatar", profile.imagesUrl).apply()
        }
    }

    private fun getCachedProfile(): UserProfile? {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val name = prefs.getString("nickname", null)
        val image = prefs.getString("avatar", null)
        return name?.let { UserProfile(display_name = it, imagesUrl = image.orEmpty()) }
    }
}