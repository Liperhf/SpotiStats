package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val recentlyPlayed = viewModel.recentlyPlayed.collectAsState()
    val isAuthenticated = viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(Unit) {
        if (isAuthenticated.value) {
            viewModel.getRecentlyPlayed()
        }
    }

    LaunchedEffect(isAuthenticated.value) {
        if (isAuthenticated.value == false) {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        }
    }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            recentlyPlayed.value?.tracks?.let { tracks ->
                items(tracks) { track ->
                    Text(track.name)
                }
            }
            item { Button(onClick = { viewModel.logout() }) { Text("Logout") } }
        }

}
