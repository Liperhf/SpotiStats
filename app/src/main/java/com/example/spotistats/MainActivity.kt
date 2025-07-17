package com.example.spotistats

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotistats.presentation.screen.authorization.AccountScreen
import com.example.spotistats.presentation.screen.authorization.AuthScreen
import com.example.spotistats.presentation.screen.authorization.AuthViewModel
import com.example.spotistats.presentation.screen.authorization.LanguageScreen
import com.example.spotistats.presentation.screen.authorization.MainScreen
import com.example.spotistats.presentation.screen.authorization.SettingsScreen
import com.example.spotistats.presentation.screen.authorization.SettingsViewModel
import com.example.spotistats.ui.theme.SpotiStatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotiStatsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel:AuthViewModel = hiltViewModel()
                    val settingsViewModel:SettingsViewModel = hiltViewModel()
                            NavHost(navController = navController, startDestination = "auth") {
                        composable("auth") {
                            AuthScreen(
                                navController = navController,
                                viewModel = authViewModel
                            )
                        }
                        composable("main") { MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            settingsViewModel = settingsViewModel) }

                        composable("settings"){ SettingsScreen(
                            navController = navController,
                            viewModel = authViewModel
                        ) }
                        composable("language"){ LanguageScreen(
                            navController = navController,
                            viewModel = settingsViewModel
                        ) }
                        composable("account") {
                            AccountScreen(
                                navController = navController,
                                viewModel = settingsViewModel
                            )

                        }
                    }

                }
            }
        }
    }
}
