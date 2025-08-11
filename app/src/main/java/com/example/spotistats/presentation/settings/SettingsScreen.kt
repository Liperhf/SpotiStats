package com.example.spotistats.presentation.settings

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spotistats.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.spotistats.presentation.auth.AuthViewModel
import com.example.spotistats.presentation.settings.components.SettingsContent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AuthViewModel
){
    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background
    val statusBarColor = MaterialTheme.colorScheme.primary
    val uiState = viewModel.uiState.collectAsState()

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }




    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = {Text(text = stringResource(R.string.settings))},
            navigationIcon = {IconButton(
                onClick = {navController.popBackStack()},
                content = { Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground) },
            )},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
        ) }
    ) {paddingValues ->
            SettingsContent(
                paddingValues = paddingValues,
                uiState = uiState.value,
                onAccountClick = {navController.navigate("account")},
                onLanguageClick = {navController.navigate("language")}
            )

    }
}
