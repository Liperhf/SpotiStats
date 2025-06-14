package com.example.spotistats

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotistats.presentation.screen.authorization.AuthScreen
import com.example.spotistats.presentation.screen.authorization.AuthViewModel
import com.example.spotistats.presentation.screen.authorization.MainScreen
import com.example.spotistats.ui.theme.SpotiStatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val authViewModel: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotiStatsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "auth") {
                        composable("auth") {
                            AuthScreen(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }
                        composable("main") { MainScreen(viewModel = authViewModel) }
                    }
                }
            }
        }
        handleSpotifyCallBack(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleSpotifyCallBack(it) }
    }

    private fun handleSpotifyCallBack(intent: Intent) {
        val data: Uri? = intent.data
        if (data?.scheme == "spotistats" && data.host == "callback") {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")

            if (code != null) {
                authViewModel.handleCallBack(code)
            } else if (error != null) {
                println("Authorization error:$error")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        authViewModel.checkAuthStatus()
    }
}

