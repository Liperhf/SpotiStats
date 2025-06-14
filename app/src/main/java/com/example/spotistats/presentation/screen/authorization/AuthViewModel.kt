package com.example.spotistats.presentation.screen.authorization

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val spotifyAuthUseCase:SpotifyAuthUseCase,
) : ViewModel() {
    private val _authIntent = MutableStateFlow<Intent?>(null)
    val authIntent: StateFlow<Intent?> = _authIntent
    private val _recentlyPlayed = MutableStateFlow<RecentlyPlayed?>(null)
    val recentlyPlayed:StateFlow<RecentlyPlayed?> = _recentlyPlayed
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun onLoginClicked(){
        _authIntent.value = spotifyAuthUseCase.createAuthIntent()
    }

    suspend fun getRecentlyPlayed(){
        _recentlyPlayed.value = spotifyAuthUseCase.getRecentlyPlayed()
    }

    fun checkAuthStatus(){
        viewModelScope.launch {
            try{
                val isSuccess = spotifyAuthUseCase.isUserAuthorized()
                _isAuthenticated.value = isSuccess
            }
            catch (e:Exception){
                _isAuthenticated.value = false
            }
        }
    }

    fun handleCallBack(code:String){
        viewModelScope.launch {
            try{
                val authDto = spotifyAuthUseCase.exchangeCodeForToken(code)
                spotifyAuthUseCase.saveAccessToken(authDto.access_token)
                checkAuthStatus()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}