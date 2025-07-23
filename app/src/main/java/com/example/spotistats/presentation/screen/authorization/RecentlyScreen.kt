package com.example.spotistats.presentation.screen.authorization

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.util.LanguagePrefs
import com.example.spotistats.util.UpdateLocale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Locale

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RecentlyScreen(
        navController: NavController,
        viewModel: AuthViewModel
    ){
        val systemUiController = rememberSystemUiController()
        val navBarColor = MaterialTheme.colorScheme.background
        val statusBarColor = MaterialTheme.colorScheme.background
        val recentlyPlayed = viewModel.recentlyPlayed.collectAsState()
        val recentlyPlayedTracks = recentlyPlayed.value?.tracks

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
                title = { Text(text = stringResource(R.string.listened_recently),
                    fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(
                        onClick = {navController.popBackStack()},
                        content = { Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onBackground) },
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
            ) }
        ) {paddingValues ->
            LazyColumn(modifier = Modifier.padding(paddingValues)){
                if (recentlyPlayedTracks != null) {
                    items(recentlyPlayedTracks.size){
                        val track = recentlyPlayedTracks[it]
                        val imageUrl = track.imageUrl
                        val name = track.name
                        val artist = track.artists
                        Row(modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 7.dp)
                            .fillMaxWidth()){
                            AsyncImage(model = imageUrl,
                                contentDescription = stringResource(R.string.listened_recently),
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                placeholder = painterResource(R.drawable.place_holder_track),
                                error = painterResource(R.drawable.place_holder_track)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Column() {
                                Text(text = name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1)

                                Text(text = artist,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                }
            }
        }