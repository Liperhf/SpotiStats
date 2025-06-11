package com.example.spotistats.presentation.screen.authorization

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val spotifyAuthUseCase:SpotifyAuthUseCase
) : ViewModel() {
    private val _authIntent = MutableStateFlow<Intent?>(null)
    val authIntent: StateFlow<Intent?> = _authIntent

    fun onLoginClicked(){
        _authIntent.value = spotifyAuthUseCase.createAuthIntent()
    }
}