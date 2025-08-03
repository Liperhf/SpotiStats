package com.example.spotistats.presentation.auth

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotistats.R
import com.example.spotistats.domain.model.Album
import com.example.spotistats.domain.model.CurrentlyPlaying
import com.example.spotistats.domain.model.RecentlyPlayed
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.UserProfile
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.domain.useCases.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()


    fun markCallbackHandled() {
        _uiState.value = _uiState.value.copy(callbackHandled = true)
    }

    fun onLoginClicked() {
        _uiState.value = _uiState.value.copy(authIntent = authUseCase.createAuthIntent())
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            try {
                val isSuccess = authUseCase.isUserAuthorized()
                Log.d("AuthViewModel", "checkAuthStatus: isUserAuthorized = $isSuccess")
                _uiState.value = _uiState.value.copy(isAuthenticated = isSuccess)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "checkAuthStatus error", e)
                _uiState.value = _uiState.value.copy(isAuthenticated = false)
            }
        }
    }

    fun processSpotifyCallback(intent: Intent) {
        val data: Uri? = intent.data
        Log.d("AuthViewModel", "processSpotifyCallback: data = $data")
        if (data?.scheme == "spotistats" && data.host == "callback") {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")
            Log.d("AuthViewModel", "processSpotifyCallback: code = $code, error = $error")
            if (code != null) {
                processAuthorizationCode(code)
            } else if (error != null) {
                Log.e("AuthViewModel", "Authorization error: $error")
            }
        }
    }

    fun processAuthorizationCode(code: String) {
        Log.d("AuthViewModel", "processAuthorizationCode: code = $code")
        viewModelScope.launch {
            try {
                val authDto = authUseCase.exchangeCodeForToken(code)
                Log.d("AuthViewModel", "exchangeCodeForToken success: $authDto")
                authUseCase.saveTokens(authDto)
                Log.d("AuthViewModel", "Tokens saved")
                checkAuthStatus()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "processAuthorizationCode error", e)
            }
        }
    }

    fun logout(){



        viewModelScope.launch {


            try{


                authUseCase.clearTokens()


                _uiState.value = _uiState.value.copy(isAuthenticated = false)


            }


            catch (e:Exception){


                e.printStackTrace()


            }


        }


    }
}



