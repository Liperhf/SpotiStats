package com.example.spotistats.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spotistats.presentation.account.AccountScreen
import com.example.spotistats.presentation.auth.AuthScreen
import com.example.spotistats.presentation.language.LanguageScreen
import com.example.spotistats.presentation.main.MainScreen
import com.example.spotistats.presentation.recently.RecentlyScreen
import com.example.spotistats.presentation.settings.SettingsScreen
import com.example.spotistats.presentation.stats.StatsScreen

@Composable
fun SpotiStatsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onStartImageCrop: (Uri, (Uri?) -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                navController = navController
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                navController = navController,
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                navController = navController
            )
        }

        composable(Screen.Language.route) {
            LanguageScreen(
                navController = navController
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(
                navController = navController,
                onStartImageCrop = onStartImageCrop
            )
        }

        composable(Screen.Stats.route) {
            StatsScreen(
            )
        }

        composable(Screen.Recently.route) {
            RecentlyScreen(
                navController = navController
            )
        }
    }
}