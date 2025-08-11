package com.example.spotistats.presentation.recently

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotistats.R
import com.example.spotistats.presentation.main.MainUiState
import com.example.spotistats.presentation.main.MainViewModel
import com.example.spotistats.presentation.recently.components.RecentlyContent
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RecentlyScreen(
        navController: NavController,
        viewModel: MainViewModel
    ){
        val systemUiController = rememberSystemUiController()
        val navBarColor = MaterialTheme.colorScheme.background
        val statusBarColor = MaterialTheme.colorScheme.background
        val mainUiState = viewModel.uiState.collectAsState()

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
            RecentlyContent(paddingValues,
                uiState = mainUiState.value
            )
            }
        }