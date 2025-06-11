package com.example.spotistats.presentation.screen.authorization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val authIntent = viewModel.authIntent.collectAsState()

    LaunchedEffect(authIntent.value) {
        authIntent.value?.let {intent ->
            context.startActivity(intent) }
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        Button(onClick = {
            viewModel.onLoginClicked()
        }) {
            Text("Login with Spotify")
        }
    }
}