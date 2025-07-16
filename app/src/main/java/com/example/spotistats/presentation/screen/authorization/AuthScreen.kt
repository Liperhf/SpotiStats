package com.example.spotistats.presentation.screen.authorization

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun AuthScreen(
    navController:NavController,
    viewModel: AuthViewModel
){
    val context = LocalContext.current
    val authIntent = viewModel.authIntent.collectAsState()
    val isAuthenticated = viewModel.isAuthenticated.collectAsState()

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

    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center){
        Button(onClick = {
            viewModel.onLoginClicked()
        },
            colors = ButtonColors(containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.background)
        )
        {
            Text(stringResource(R.string.login))
        }
    }
}