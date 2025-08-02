package com.example.spotistats.presentation.auth

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spotistats.R
import com.example.spotistats.presentation.auth.components.AuthContent

@Composable
fun AuthScreen(
    navController:NavController,
    viewModel: AuthViewModel
){
    val context = LocalContext.current
    val activity = context as? Activity
    val authIntent = viewModel.authIntent.collectAsState()
    val isAuthenticated = viewModel.isAuthenticated.collectAsState()


    LaunchedEffect(Unit) {
        val intent = activity?.intent
        if (intent?.data != null && !viewModel.callbackHandled.value){
            viewModel.processSpotifyCallback(intent)
            viewModel.markCallbackHandled()
        } else {
            viewModel.checkAuthStatus()
        }
    }

    LaunchedEffect(authIntent.value) {
        authIntent.value?.let {intent ->
            context.startActivity(intent) }
    }

    LaunchedEffect(isAuthenticated.value) {
        if(isAuthenticated.value){
            navController.navigate("main"){
                popUpTo("auth"){inclusive = true}
            }
        }
    }
    AuthContent(
        onLoginClicked = viewModel::onLoginClicked
    )

}