package com.example.spotistats.navigation

sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Main : Screen("main")
    data object Settings : Screen("settings")
    data object Language : Screen("language")
    data object Account : Screen("account")
    data object Stats : Screen("stats")
    data object Recently : Screen("recently")
}