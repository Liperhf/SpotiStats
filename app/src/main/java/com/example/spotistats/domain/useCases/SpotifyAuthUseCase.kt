package com.example.spotistats.domain.useCases

import android.content.Intent
import com.example.spotistats.data.api.models.SpotifyAuthResponse
import com.example.spotistats.data.repository.RepositoryImpl
import com.example.spotistats.domain.Repository
import javax.inject.Inject

class SpotifyAuthUseCase @Inject constructor(
    private val repository:Repository
) {
    fun createAuthIntent():Intent{
        return repository.buildAuthIntent()
    }

    suspend fun exchangeCodeForToken(code:String):SpotifyAuthResponse{
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
}