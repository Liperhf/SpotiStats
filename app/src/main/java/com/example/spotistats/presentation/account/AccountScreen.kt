package com.example.spotistats.presentation.account

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.spotistats.R
import com.example.spotistats.presentation.account.components.AccountContent
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    navController: NavController,
    onStartImageCrop: (sourceUri: Uri, callback: (Uri?) -> Unit) -> Unit
){
    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background
    val statusBarColor = MaterialTheme.colorScheme.primary
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current


    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){
        uri:Uri? ->
        uri?.let { sourceImageUri ->
            onStartImageCrop(sourceImageUri){croppedUri->
                croppedUri?.let {
                    viewModel.setAvatar(croppedUri)
                }
            }

        }
    }

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
            title = { Text(text = stringResource(R.string.account)) },
            navigationIcon = {
                IconButton(
                onClick = {navController.popBackStack()},
                content = { Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground) },
            )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
        ) },
    ) {paddingValues ->
                AccountContent(
                    paddingValues = paddingValues,
                    pickImageLauncher = pickImageLauncher,
                    imageUri = uiState.value.imageUrl,
                    nickname = uiState.value.nickname,
                    context = context,
                    onSetNickNameClick = viewModel::setNickName,
                    onSaveProfileClick = viewModel::saveProfile,
                    onResetProfileClick = viewModel::resetProfile
                )


                }
    }
