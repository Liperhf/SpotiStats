package com.example.spotistats.presentation.screen.authorization

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
        try {
            _recentlyPlayed.value = spotifyAuthUseCase.getRecentlyPlayed()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun logout(){
        viewModelScope.launch {
            try{
                spotifyAuthUseCase.clearTokens()
                _isAuthenticated.value = false
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun processAuthorizationCode(code:String){
        viewModelScope.launch {
            try{
                val authDto = spotifyAuthUseCase.exchangeCodeForToken(code)
                spotifyAuthUseCase.saveTokens(authDto)
                checkAuthStatus()
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun processSpotifyCallback(intent: Intent) {
        val data: Uri? = intent.data
        if (data?.scheme == "spotistats" && data.host == "callback") {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")

            if (code != null) {
                processAuthorizationCode(code)
            } else if (error != null) {
                println("Authorization error:$error")
            }
        }
    }

    fun refreshToken(){
        viewModelScope.launch {
            try {
                val success = spotifyAuthUseCase.refreshTokenIfNeeded()
                if (!success) {
                    _isAuthenticated.value = false
                }
            }catch (e:Exception){
                e.printStackTrace()
                _isAuthenticated.value = false
            }
        }
    }


}