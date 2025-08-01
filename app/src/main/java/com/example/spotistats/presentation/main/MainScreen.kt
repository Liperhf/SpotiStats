package com.example.spotistats.presentation.main

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.TopAlbum
import com.example.spotistats.domain.model.Track
import com.example.spotistats.domain.model.UserTopArtists
import com.example.spotistats.domain.model.UserTopTracks
import com.example.spotistats.presentation.auth.AuthViewModel
import com.example.spotistats.presentation.main.components.DrawerContent
import com.example.spotistats.presentation.main.components.DrawerItem
import com.example.spotistats.presentation.main.components.LastPlayedBox
import com.example.spotistats.presentation.main.components.MainContent
import com.example.spotistats.presentation.main.components.NowPlayingBox
import com.example.spotistats.presentation.main.components.RecentlyPlayedBox
import com.example.spotistats.presentation.main.components.TrackProgressBar
import com.example.spotistats.presentation.main.components.UserTopAlbumsBox
import com.example.spotistats.presentation.main.components.UserTopArtistsBox
import com.example.spotistats.presentation.main.components.UserTopTracksBox
import com.example.spotistats.presentation.settings.SettingsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val recentlyPlayed = mainViewModel.recentlyPlayed.collectAsState()
    val recentlyPlayedTracks = recentlyPlayed.value?.tracks
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState()
    val greeting = stringResource(id = mainViewModel.getGreeting())
    val userProfile = mainViewModel.userProfile.collectAsState()
    val currentlyPlaying = mainViewModel.currentlyPlaying.collectAsState()
    val progressMs = mainViewModel.progressMs.collectAsState()
    val lastPlayedTrack = recentlyPlayed.value?.tracks?.firstOrNull()
    val lastPlayedImageUrl = lastPlayedTrack?.imageUrl
    val lastPlayedName = lastPlayedTrack?.name
    val lastPlayedArtist = lastPlayedTrack?.artists
    val currentlyImageUrl = currentlyPlaying.value?.item?.imageUrl
    val currentlyTrackName = currentlyPlaying.value?.item?.name
    val currentlyArtistName = currentlyPlaying.value?.item?.artists
    val currentlyTrack = currentlyPlaying.value?.item
    val currentlyDurationMs = currentlyTrack?.duration_ms ?: 1
    val currentlyUserName = settingsViewModel.nickname.collectAsState()
    val currentlyUserAvatar = settingsViewModel.imageUri.collectAsState()
    val userTopArtists = mainViewModel.userTopArtists.collectAsState()
    val userTopTracks = mainViewModel.userTopTracks.collectAsState()
    val userTopAlbums = mainViewModel.userTopAlbums.collectAsState()

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
        if (isAuthenticated.value) {
            try {
                mainViewModel.getRecentlyPlayed()
            } catch (e: Exception) {
                Log.e("MainScreen", "Failed to get recently played: ${e.message}")
            }

            try {
                mainViewModel.getUserProfile()
            } catch (e: Exception) {
                Log.e("MainScreen", "Failed to get profile: ${e.message}")
            }

            try {
                mainViewModel.getCurrentlyPlaying()
            } catch (e: Exception) {
                Log.e("MainScreen", "Failed to get currently playing: ${e.message}")
            }

            try {
                mainViewModel.getUserTopArtists()
            } catch (e: Exception) {
                Log.e("MainScreen", "Failed to get top artists: ${e.message}")
            }

            try {
                mainViewModel.getUserTopTracks()
            } catch (e: Exception) {
                Log.e("MainScreen", "Failed to get top tracks: ${e.message}")
            }

        }
    }


    LaunchedEffect(isAuthenticated.value) {
        if (!isAuthenticated.value) {
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
                    currentlyUserAvatar,
                    currentlyUserName,
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
                                        model = currentlyUserAvatar.value,
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
                MainContent(
                    isRefreshing,
                    refreshRecentlyPlayed,
                    paddingValues,
                    currentlyPlaying,
                    currentlyImageUrl,
                    currentlyTrackName,
                    currentlyArtistName,
                    currentlyDurationMs,
                    progressMs,
                    lastPlayedTrack?.name,
                    lastPlayedImageUrl,
                    lastPlayedTrack?.album,
                    lastPlayedName,
                    lastPlayedArtist,
                    recentlyPlayedTracks,
                    onRecentlyShowClick = {navController.navigate("recently")},
                    userTopArtists,
                    userTopTracks,
                    userTopAlbums
                )
            }
        }
    }









