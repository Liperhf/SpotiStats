package com.example.spotistats.presentation.recently

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.R
import com.example.spotistats.navigation.Screen
import com.example.spotistats.presentation.main.MainViewModel
import com.example.spotistats.presentation.recently.components.RecentlyContent
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentlyScreen(
    navController: NavController,
) {

    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background
    val statusBarColor = MaterialTheme.colorScheme.background
    val viewModel: MainViewModel = hiltViewModel()
    val mainUiState = viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRecentlyPlayed()
    }

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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.listened_recently),
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            )
        }
    ) { paddingValues ->
        RecentlyContent(
            paddingValues,
            uiState = mainUiState.value
        )
    }
}