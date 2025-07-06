package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.view.textclassifier.TextClassifierEvent.TextLinkifyEvent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults.contentWindowInsets
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import kotlinx.coroutines.delay
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
    val progressMs = viewModel.progressMs.collectAsState()

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
                delay(1000)
            }
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    AsyncImage(model = userProfile.value?.imagesUrl, contentDescription = "", modifier = Modifier
                        .padding(start = 8.dp)
                        .size(45.dp)
                        .clip(CircleShape))
                    userProfile.value?.display_name?.let { Text(it, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold, fontSize = 20.sp) }
                }
            HorizontalDivider(color = MaterialTheme.colorScheme.primary)
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ){paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            val imageUrl = currentlyPlaying.value?.item?.imageUrl
            val trackName = currentlyPlaying.value?.item?.name
            val artistName = currentlyPlaying.value?.item?.artists
            val track = currentlyPlaying.value?.item
            val durationMs = track?.duration_ms ?: 1
            if(imageUrl != null && trackName != null && artistName != null){
            item {
                NowPlayingBox(imageUrl,trackName,artistName,durationMs,progressMs.value)
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
            Text("Now Playing",
                modifier = Modifier.padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,)
            Row {
                AsyncImage(model = imageUrl, contentDescription = "", modifier = Modifier
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
        icon = { Icon(imageVector = icon, contentDescription = "") },
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
            Text(text = formatTime(durationMs), modifier = Modifier.padding(start = 170.dp,top = 4.dp), fontSize = 14.sp)
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

