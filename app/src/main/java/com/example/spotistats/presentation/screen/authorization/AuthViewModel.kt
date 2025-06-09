package com.example.spotistats.presentation.screen.authorization

import androidx.lifecycle.ViewModel
import com.example.spotistats.domain.useCases.SpotifyAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val spotifyAuthUseCase:SpotifyAuthUseCase
) : ViewModel() {
    fun onLoginClicked(){

    }
}