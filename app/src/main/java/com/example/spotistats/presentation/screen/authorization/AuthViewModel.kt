package com.example.spotistats.presentation.screen.authorization

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.R
import com.example.spotistats.data.dto.RecentlyPlayedDto
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
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
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile:StateFlow<UserProfile?> = _userProfile
    private val _currentlyPlaying = MutableStateFlow<CurrentlyPlaying?>(null)
    val currentlyPlaying:StateFlow<CurrentlyPlaying?> = _currentlyPlaying


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


    @RequiresApi(Build.VERSION_CODES.O)
    fun getGreeting():Int{
        val hour = LocalTime.now().hour
        val resId = when(hour){
            in 6..11 -> R.string.good_morning
            in 12..17 -> R.string.good_day
            in 18..24 -> R.string.good_evening
            else -> R.string.good_night
        }
        return resId
    }


    suspend fun getUserProfile(){
        try {
            _userProfile.value = spotifyAuthUseCase.getUserProfile()
            println("Profile loaded: ${_userProfile.value?.imagesUrl}")
        } catch (e: Exception) {
            println("Failed to load profile: ${e.message}")
        }
    }
    suspend fun getCurrentlyPlaying(){
        try {
            _currentlyPlaying.value = spotifyAuthUseCase.getCurrentlyPlaying()
        }catch (e:Exception){
            _currentlyPlaying.value = null
            Log.e("NowPlaying","Failed to load : ${e.message}")
        }
    }
}