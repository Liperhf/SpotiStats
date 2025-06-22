package com.example.spotistats.domain.useCases

import android.content.Intent
import com.example.spotistats.data.dto.AuthTokenDto
import com.example.spotistats.domain.Repository
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.Item
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import javax.inject.Inject

class SpotifyAuthUseCase @Inject constructor(
    private val repository:Repository
) {
    fun createAuthIntent():Intent{
        return repository.buildAuthIntent()
    }

    suspend fun exchangeCodeForToken(code:String): AuthTokenDto {
        return repository.exchangeCodeForToken(code)
    }

    suspend fun saveTokens(authTokenDto: AuthTokenDto){
        return repository.saveTokens(authTokenDto)
    }

    suspend fun getAccessToken():String?{
        return repository.getAccessToken()
    }

    suspend fun getRefreshToken():String?{
        return repository.getRefreshToken()
    }

    suspend fun refreshTokenIfNeeded():Boolean{
        return try {
            if(isTokenExpired()){
                val refreshToken = getRefreshToken()
                if(refreshToken != null){
                    val newAuthDto = repository.refreshToken(refreshToken)
                    saveTokens(newAuthDto)
                    true
                }else false
            }else true
        }catch (e:Exception){
            false
        }
    }

    suspend fun getExpiresAt():Long{
        return repository.getExpiresAt()
    }


    suspend fun isUserAuthorized():Boolean{
        val token = getAccessToken()
        if(token == null) return false
        if(isTokenExpired()){
            return refreshTokenIfNeeded()
        }
        return true
    }

    suspend fun getRecentlyPlayed():RecentlyPlayed{
        return repository.getRecentlyPlayed()
    }

    suspend fun clearTokens(){
        return repository.clearTokens()
    }

    suspend fun isTokenExpired():Boolean{
        return repository.isTokenExpired()
    }
    suspend fun getUserProfile():UserProfile{
        return repository.getUserProfile()
    }
    suspend fun getCurrentlyPlaying():CurrentlyPlaying{
        return repository.getCurrentlyPlaying()
    }
}