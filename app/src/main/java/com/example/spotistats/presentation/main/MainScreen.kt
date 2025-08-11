package com.example.spotistats.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.presentation.auth.AuthViewModel
import com.example.spotistats.presentation.main.components.DrawerContent
import com.example.spotistats.presentation.main.components.MainContent
import com.example.spotistats.presentation.account.AccountViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel:MainViewModel,
    authViewModel:AuthViewModel,
    settingsViewModel:AccountViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val greeting = stringResource(id = mainViewModel.getGreeting())
    val accountUiState = settingsViewModel.uiState.collectAsState()
    val mainUiState = mainViewModel.uiState.collectAsState()
    val authUiState = authViewModel.uiState.collectAsState()

    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background

    val isRefreshing = remember { mutableStateOf(false) }

    val refreshRecentlyPlayed = {
        scope.launch {
            isRefreshing.value = true
            mainViewModel.getRecentlyPlayed()
            delay(1000)
            isRefreshing.value = false
        }
    }

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )
        systemUiController.setStatusBarColor(
            color = navBarColor,
            darkIcons = false
        )
    }

    LaunchedEffect(Unit) {
        if (authUiState.value.isAuthenticated) {
            mainViewModel.loadStats()
        }
    }


    LaunchedEffect(authUiState.value.isAuthenticated) {
        if (!authUiState.value.isAuthenticated) {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        } else {
            while (true) {
                try {
                    mainViewModel.getCurrentlyPlaying()
                } catch (e: Exception) {
                    Log.e("MainScreen", "Ошибка при обновлении now playing: ${e.message}")
                }
                delay(1000)
            }
        }
    }


        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(
                    accountUiState.value.imageUrl,
                    accountUiState.value.nickname,
                    onSettingsClick = { navController.navigate("settings") },
                    onLogoutClick = {authViewModel.logout()}
                )
            },
            drawerState = drawerState,
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(greeting) },
                        navigationIcon = {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                content = {
                                    AsyncImage(
                                        model = accountUiState.value.imageUrl,
                                        contentDescription = stringResource(R.string.avatar),
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor =
                            MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
            ) { paddingValues ->
                if(mainUiState.value.isLoading){
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onBackground)
                    }
                }
                MainContent(
                    isRefreshing,
                    refreshRecentlyPlayed,
                    paddingValues,
                    onRecentlyShowClick = {navController.navigate("recently")},
                    mainUiState = mainUiState.value
                )
            }
        }
    }









