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
    settingsViewModel: SettingsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val recentlyPlayed = mainViewModel.recentlyPlayed.collectAsState()
    val recentlyPlayedTracks = recentlyPlayed.value?.tracks
    val isAuthenticated = mainViewModel.isAuthenticated.collectAsState()
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
                ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = currentlyUserAvatar.value,
                            contentDescription = stringResource(R.string.avatar),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(45.dp)
                                .clip(CircleShape)
                        )
                        currentlyUserName.value.let {
                            if (it != null) {
                                Text(
                                    it,
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.primary)
                    DrawerItem(text = stringResource(R.string.settings), onClick = {
                        navController.navigate("settings")
                    }, icon = Icons.Default.Settings)
                    DrawerItem(
                        text = stringResource(R.string.logout),
                        onClick = { mainViewModel.logout() },
                        icon = Icons.Default.Delete
                    )
                }
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
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                    onRefresh = { refreshRecentlyPlayed() },
                ) {
                    LazyColumn(modifier = Modifier.padding(paddingValues)) {

                        item {
                            when {
                                currentlyPlaying.value?.is_playing == true -> {
                                    if (currentlyImageUrl != null && currentlyTrackName != null && currentlyArtistName != null) {
                                        NowPlayingBox(
                                            currentlyImageUrl,
                                            currentlyTrackName,
                                            currentlyArtistName,
                                            currentlyDurationMs,
                                            progressMs.value
                                        )
                                    }
                                }

                                lastPlayedTrack != null -> {
                                    if (lastPlayedImageUrl != null && lastPlayedName != null && lastPlayedArtist != null) {
                                        LastPlayedBox(
                                            lastPlayedImageUrl,
                                            lastPlayedName,
                                            lastPlayedArtist
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            if (recentlyPlayedTracks != null) {
                                RecentlyPlayedBox(
                                    recentlyPlayedTracks = recentlyPlayedTracks,
                                    navController
                                )
                            }
                        }
                        item {
                            userTopArtists.value?.let { UserTopArtistsBox(it) }
                        }
                        item {
                            userTopTracks.value?.let { UserTopTracksBox(it) }
                        }
                        item {
                            UserTopAlbumsBox(userTopAlbums.value)
                        }
                    }

                }
            }
        }
    }



    @Composable
    fun NowPlayingBox(
        imageUrl: String,
        name: String,
        artist: String,
        durationMs: Int,
        progressMs: Int
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .height(160.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.now_playing),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Row {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.track_image),
                        modifier = Modifier
                            .size(105.dp, 105.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                        Text(
                            name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            artist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        TrackProgressBar(progressMs, durationMs)
                    }
                }
            }
        }
    }

    @Composable
    fun LastPlayedBox(imageUrl: String, name: String, artist: String) {
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .height(160.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.last_played),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Row {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = stringResource(R.string.track_image),
                        modifier = Modifier
                            .size(105.dp, 105.dp)
                            .clip(RoundedCornerShape(10.dp),),
                        placeholder = painterResource(R.drawable.place_holder_track),
                        error = painterResource(R.drawable.place_holder_track)
                    )
                    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                        Text(
                            name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            artist,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun RecentlyPlayedBox(recentlyPlayedTracks: List<Track>, navController: NavController) {
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .height(250.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.listened_recently),
                        modifier = Modifier.padding(bottom = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(text = stringResource(R.string.show_all),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .clickable {
                                navController.navigate("recently")
                            })
                }
                LazyRow() {
                    items(recentlyPlayedTracks.take(20).size) {
                        val track = recentlyPlayedTracks[it]
                        val imageUrl = track.imageUrl
                        val title = track.name
                        val artist = track.artists
                        Column(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(7.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = stringResource(R.string.track_image),
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                placeholder = painterResource(R.drawable.place_holder_track),
                                error = painterResource(R.drawable.place_holder_track)
                            )
                            Text(
                                title, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = artist,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun UserTopTracksBox(userTopTracks: UserTopTracks) {
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .height(250.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.top_tracks_month),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                LazyRow() {
                    items(userTopTracks.items.take(10).size) {
                        val track = userTopTracks.items[it]
                        val imageUrl = track.album.images.firstOrNull()?.url
                        val title = track.name
                        val artist = track.artists.joinToString(",") { it.name }
                        Column(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(7.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = stringResource(R.string.track_image),
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.place_holder_track)
                            )
                            Text(
                                title, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (artist != null) {
                                Text(
                                    text = artist,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun UserTopArtistsBox(userTopArtists: UserTopArtists) {
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .height(250.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.top_artists_month),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                LazyRow() {
                    items(userTopArtists.items.take(10).size) {
                        val artist = userTopArtists.items[it]
                        val imageUrl = artist.images.firstOrNull()?.url
                        val title = artist.name
                        Column(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(7.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = stringResource(R.string.track_image),
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.place_holder_artist)
                            )
                            Text(
                                title, fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun UserTopAlbumsBox(userTopAlbums: List<TopAlbum?>) {
        Box(
            modifier = Modifier
                .padding(top = 30.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .height(250.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = stringResource(R.string.top_albums_month),
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                LazyRow() {
                    items(userTopAlbums.size) {
                        val album = userTopAlbums[it]?.album
                        val artist = album?.artists?.firstOrNull()?.name
                        val imageUrl = album?.images?.firstOrNull()?.url
                        val title = album?.name
                        Column(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(7.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = stringResource(R.string.track_image),
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.place_holder_track)
                            )
                            if (title != null) {
                                Text(
                                    title, fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (artist != null) {
                                Text(
                                    text = artist,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun DrawerItem(
        text: String,
        onClick: () -> Unit,
        icon: ImageVector
    ) {
        NavigationDrawerItem(
            label = { Text(text) },
            selected = false,
            onClick = { onClick() },
            modifier = Modifier,
            icon = { Icon(imageVector = icon, contentDescription = stringResource(R.string.icon)) },
            colors = NavigationDrawerItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onBackground,
                unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                selectedContainerColor = MaterialTheme.colorScheme.background,
                unselectedContainerColor = MaterialTheme.colorScheme.background,
                selectedTextColor = MaterialTheme.colorScheme.onBackground,
                unselectedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }


    @Composable
    fun TrackProgressBar(
        progressMs: Int,
        durationMs: Int
    ) {
        val progress = if (durationMs > 0) progressMs.toFloat() / durationMs else 0f

        Column {
            Row() {
                Text(
                    text = formatTime(progressMs),
                    modifier = Modifier
                        .padding(top = 4.dp),
                    fontSize = 14.sp
                )
                Text(
                    text = formatTime(durationMs),
                    modifier = Modifier.padding(start = 170.dp, top = 4.dp),
                    fontSize = 14.sp
                )
            }
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }
