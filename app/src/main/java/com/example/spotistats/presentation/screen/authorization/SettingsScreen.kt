package com.example.spotistats.presentation.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: AuthViewModel
){
    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background
    val statusBarColor = MaterialTheme.colorScheme.primary
    val isAuthenticated = viewModel.isAuthenticated.collectAsState()

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
            title = {Text(text = stringResource(R.string.settings))},
            navigationIcon = {IconButton(
                onClick = {navController.popBackStack()},
                content = { Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground) },
            )},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
        ) }
    ) {paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            if (isAuthenticated.value) {
                item{
                    SettingsItem(title = stringResource(R.string.account),
                        icon = Icons.Default.AccountCircle,
                        onClick = {navController.navigate("account")})
                }
                item{
                    SettingsItem(title = stringResource(R.string.language),
                        icon = ImageVector.vectorResource(R.drawable.language_24px),
                        onClick = {navController.navigate("language")}
                    )
                }
            }
        }

    }
}

@Composable
fun SettingsItem(
    title:String,
    icon:ImageVector,
    onClick: () -> Unit){
    ListItem(
        headlineContent = { Text(title, fontSize = 18.sp) },
        leadingContent = {Icon(imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(35.dp)
        )},
        modifier = Modifier.clickable { onClick() }
            .height(70.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            leadingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}