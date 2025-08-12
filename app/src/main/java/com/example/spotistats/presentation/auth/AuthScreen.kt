package com.example.spotistats.presentation.auth

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.presentation.auth.components.AuthContent

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val uiState = viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        val intent = activity?.intent
        if (intent?.data != null && !uiState.value.callbackHandled) {
            viewModel.processSpotifyCallback(intent)
            viewModel.markCallbackHandled()
        } else {
            viewModel.checkAuthStatus()
        }
    }

    LaunchedEffect(uiState.value.authIntent) {
        uiState.value.authIntent?.let { intent ->
            context.startActivity(intent)
        }
    }

    LaunchedEffect(uiState.value.isAuthenticated) {
        if (uiState.value.isAuthenticated) {
            navController.navigate("main") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }
    AuthContent(
        onLoginClicked = viewModel::onLoginClicked
    )

}