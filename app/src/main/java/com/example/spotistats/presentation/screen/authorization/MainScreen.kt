package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val recentlyPlayed = authViewModel.recentlyPlayed.collectAsState()
    val isAuthenticated = authViewModel.isAuthenticated.collectAsState()
    val greeting = stringResource(id = authViewModel.getGreeting())
    val userProfile = authViewModel.userProfile.collectAsState()
    val currentlyPlaying = authViewModel.currentlyPlaying.collectAsState()
    val progressMs = authViewModel.progressMs.collectAsState()
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

    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background

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
            authViewModel.getRecentlyPlayed()
            authViewModel.getUserProfile()
            authViewModel.getCurrentlyPlaying()
        }
    }

    LaunchedEffect(isAuthenticated.value) {
        if (isAuthenticated.value == false) {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        }else{
            while(true){
                authViewModel.getCurrentlyPlaying()
                delay(1000)
            }
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    AsyncImage(model = currentlyUserAvatar.value,
                        contentDescription = stringResource(R.string.avatar),
                        modifier = Modifier
                        .padding(start = 8.dp)
                        .size(45.dp)
                        .clip(CircleShape))
                    currentlyUserName.value.let {
                        if (it != null) {
                            Text(it, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
            DrawerItem(text = stringResource(R.string.settings), onClick = {
                navController.navigate("settings")
            },icon = Icons.Default.Settings)
            DrawerItem(text = stringResource(R.string.logout), onClick = { authViewModel.logout() },icon = Icons.Default.Delete)
        }
        },
        drawerState = drawerState,
    ){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(greeting)},
                navigationIcon = {IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    content = { AsyncImage(model = currentlyUserAvatar.value,
                        contentDescription = stringResource(R.string.avatar),
                        modifier = Modifier.size(40.dp)) }
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ){paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {

            item {
                when{
                    currentlyPlaying.value?.is_playing == true -> {
                        if(currentlyImageUrl != null && currentlyTrackName != null && currentlyArtistName != null){
                            NowPlayingBox(currentlyImageUrl,currentlyTrackName,currentlyArtistName,currentlyDurationMs,progressMs.value)
                        }
                    }
                    lastPlayedTrack != null -> {
                        if (lastPlayedImageUrl != null && lastPlayedName != null && lastPlayedArtist != null) {
                            LastPlayedBox(lastPlayedImageUrl,lastPlayedName,lastPlayedArtist)
                        }
                    }
                }
            } } }
        }
    }



@Composable
fun NowPlayingBox(imageUrl:String,name:String,artist:String,durationMs: Int,progressMs: Int){
    Box(modifier = Modifier
        .padding(horizontal = 10.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.primary)
        .height(160.dp)){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = stringResource(R.string.now_playing) ,
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,)
            Row {
                AsyncImage(model = imageUrl, contentDescription = stringResource(R.string.track_image), modifier = Modifier
                    .size(105.dp, 105.dp)
                    .clip(RoundedCornerShape(10.dp))
                    )
                Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                    Text(name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    Text(artist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    TrackProgressBar(progressMs,durationMs)
                }
            }
        }
    }
}

@Composable
fun LastPlayedBox(imageUrl:String,name:String,artist: String){
    Box(modifier = Modifier
        .padding(horizontal = 10.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.primary)
        .height(160.dp)
        .fillMaxWidth()){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = stringResource(R.string.last_played),
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,)
            Row {
                AsyncImage(model = imageUrl, contentDescription = stringResource(R.string.track_image), modifier = Modifier
                    .size(105.dp, 105.dp)
                    .clip(RoundedCornerShape(10.dp))
                )
                Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                    Text(name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                    Text(artist,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}


@Composable
fun DrawerItem(
    text: String,
    onClick: () -> Unit,
    icon:ImageVector
){
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
        Row(){
            Text(
                text = formatTime(progressMs),
                modifier = Modifier
                    .padding(top = 4.dp),
                fontSize = 14.sp
            )
            Text(text = formatTime(durationMs),
                modifier = Modifier.padding(start = 170.dp,top = 4.dp),
                fontSize = 14.sp)
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
fun formatTime(ms:Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d",minutes,seconds)
}

