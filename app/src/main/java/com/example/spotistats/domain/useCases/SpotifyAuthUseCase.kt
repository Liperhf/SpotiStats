package com.example.spotistats.domain.useCases

import android.content.Intent
import com.example.spotistats.data.dto.AuthorizationDto
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.domain.Repository
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.Track
import javax.inject.Inject

class SpotifyAuthUseCase @Inject constructor(
    private val repository:Repository
) {
    fun createAuthIntent():Intent{
        return repository.buildAuthIntent()
    }

    suspend fun exchangeCodeForToken(code:String): AuthorizationDto {
        return repository.exchangeCodeForToken(code)
    }

    suspend fun saveAccessToken(token:String){
        return repository.saveAccessToken(token)
    }

    suspend fun getAccessToken():String?{
        return repository.getAccessToken()
    }

    suspend fun isUserAuthorized():Boolean{
    return getAccessToken() != null
    }

    suspend fun getRecentlyPlayed():RecentlyPlayed{
        return repository.getRecentlyPlayed()
    }


}