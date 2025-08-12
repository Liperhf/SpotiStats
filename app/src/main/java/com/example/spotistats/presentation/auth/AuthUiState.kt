package com.example.spotistats.presentation.auth

import android.content.Intent

data class AuthUiState(
    val authIntent: Intent? = null,
    val isAuthenticated: Boolean = false,
    val callbackHandled: Boolean = false,
    val errorMessage: String? = null,
    val hasCheckedAuth: Boolean = false
)