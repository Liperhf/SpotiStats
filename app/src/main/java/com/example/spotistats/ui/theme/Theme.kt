package com.example.spotistats.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackGround,
    onBackground = OnBackGround,
    onSurface = OnSurface,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackGround,
    onBackground = OnBackGround,
    onSurface = OnSurface,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary

)

@Composable
fun SpotiStatsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false
    val navBarColor = BackGround
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = useDarkIcons
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,
            darkIcons = useDarkIcons
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}