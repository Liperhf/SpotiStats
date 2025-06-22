package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val recentlyPlayed = viewModel.recentlyPlayed.collectAsState()
    val isAuthenticated = viewModel.isAuthenticated.collectAsState()
    val greeting = stringResource(id = viewModel.getGreeting())
    val userProfile = viewModel.userProfile.collectAsState()
    val currentlyPlaying = viewModel.currentlyPlaying.collectAsState()

    LaunchedEffect(Unit) {
        if (isAuthenticated.value) {
            viewModel.getRecentlyPlayed()
            viewModel.getUserProfile()
            viewModel.getCurrentlyPlaying()
        }
    }

    LaunchedEffect(isAuthenticated.value) {
        if (isAuthenticated.value == false) {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        }else{
            while(true){
                viewModel.getCurrentlyPlaying()
                kotlinx.coroutines.delay(3000)
            }
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
            userProfile.value?.display_name?.let { Text(it, modifier = Modifier.padding(16.dp)) }
            HorizontalDivider()
            DrawerItem(text = stringResource(R.string.settings), onClick = {
                navController.navigate("settings")
            },icon = Icons.Default.Settings)
            DrawerItem(text = stringResource(R.string.logout), onClick = { viewModel.logout() },icon = Icons.Default.Delete)
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
                    content = { AsyncImage(model = userProfile.value?.imagesUrl,
                        contentDescription = "",
                        modifier = Modifier.size(40.dp)) }
                )},
            )
        },
    ){paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                Box(modifier = Modifier.padding(paddingValues)){
                    Icon(imageVector = Icons.Default.Home,
                        contentDescription = "",
                        modifier = Modifier
                        .height(40.dp)
                        .width(40.dp))
            }
            }
            val imageUrl = userProfile.value?.imagesUrl
            val trackName = currentlyPlaying.value?.item?.name
            val artistName = currentlyPlaying.value?.item?.artists
            val progressMs = currentlyPlaying.value?.progress_ms ?: 0
            val track = currentlyPlaying.value?.item
            val durationMs = track?.duration_ms ?: 1
            if(imageUrl != null && trackName != null && artistName != null){
            item {
                NowPlayingBox(imageUrl,trackName,artistName,durationMs,progressMs)
                }
            } } }
        }
    }



@Composable
fun NowPlayingBox(imageUrl:String,name:String,artist:String,durationMs: Int,progressMs: Int){
    Box(){
        Column {
            Text("Now Playing")
            Row {
                AsyncImage(model = imageUrl, contentDescription = "")
                Column {
                    Text(name)
                    Text(artist)
                    TrackProgressBar(progressMs,durationMs)

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
) {
    ListItem(
        headlineContent = { Text(text) },
        modifier = Modifier.clickable { onClick() },
        leadingContent = { Icon(imageVector = icon, contentDescription = "") },
    )
}


@Composable
fun TrackProgressBar(
    progressMs: Int,
    durationMs: Int
) {
    val progress = if (durationMs > 0) progressMs.toFloat() / durationMs else 0f

    Column {
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
        )
        Text(
            text = "${(progressMs / 1000)} / ${(durationMs / 1000)} сек",
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}//разбор и фиксы до конца,ну и viewmodel тожеее