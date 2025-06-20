package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

    LaunchedEffect(Unit) {
        if (isAuthenticated.value) {
            viewModel.getRecentlyPlayed()
        }
    }

    LaunchedEffect(isAuthenticated.value) {
        if (isAuthenticated.value == false) {
            navController.navigate("auth") {
                popUpTo("main") { inclusive = true }
            }
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
            Text("Nickname", modifier = Modifier.padding(16.dp))
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
                    content = {Icon(imageVector = Icons.Default.Menu, contentDescription = "")}
                )},
            )
        },
    ){paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {

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