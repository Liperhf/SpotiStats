package com.example.spotistats

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spotistats.presentation.screen.authorization.AccountScreen
import com.example.spotistats.presentation.screen.authorization.AppBottomNavigationBar
import com.example.spotistats.presentation.screen.authorization.AuthScreen
import com.example.spotistats.presentation.screen.authorization.AuthViewModel
import com.example.spotistats.presentation.screen.authorization.LanguageScreen
import com.example.spotistats.presentation.screen.authorization.MainScreen
import com.example.spotistats.presentation.screen.authorization.RecentlyScreen
import com.example.spotistats.presentation.screen.authorization.SettingsScreen
import com.example.spotistats.presentation.screen.authorization.SettingsViewModel
import com.example.spotistats.presentation.screen.authorization.StatsScreen
import com.example.spotistats.ui.theme.SpotiStatsTheme
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    private lateinit var uCropLauncher: ActivityResultLauncher<Intent>
    private var currentCropCallback:((Uri?) -> Unit)? = null


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uCropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intentData ->
                    UCrop.getOutput(intentData)?.let { resultUri ->
                        currentCropCallback?.invoke(resultUri)
                    } ?: currentCropCallback?.invoke(null)
                } ?: currentCropCallback?.invoke(null)
            } else if (result.resultCode == UCrop.RESULT_ERROR) {
                val cropError = result.data?.let { UCrop.getError(it) }
                Log.e("UCROP_MAIN", "Crop error: $cropError")
                currentCropCallback?.invoke(null)
            } else {
                currentCropCallback?.invoke(null)
            }
            currentCropCallback = null
        }

        setContent {
            SpotiStatsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel:AuthViewModel = hiltViewModel()
                    val settingsViewModel:SettingsViewModel = hiltViewModel()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val startImageCrop: (Uri, (Uri?) -> Unit) -> Unit = { source, callback ->
                        startCropActivityForResult(source, callback)
                    }
                    val screensWithBottomBar = listOf("main","stats","settings","language","account","recently")
                    val shouldShowBottomBar = currentRoute in screensWithBottomBar

                    Scaffold(
                            bottomBar =
                            {
                                if(shouldShowBottomBar) {
                                AppBottomNavigationBar(navController)
                            }
                            }
                    ) {
                        paddingValues ->
                        NavHost(navController = navController,
                            startDestination = "auth",
                            modifier = Modifier.padding(paddingValues)) {
                            composable("auth") {
                                AuthScreen(
                                    navController = navController,
                                    viewModel = authViewModel
                                )
                            }
                            composable("main") { MainScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                settingsViewModel = settingsViewModel) }

                            composable("settings"){ SettingsScreen(
                                navController = navController,
                                viewModel = authViewModel
                            ) }
                            composable("language"){ LanguageScreen(
                                navController = navController,
                                viewModel = settingsViewModel
                            ) }
                            composable("account") {
                                AccountScreen(
                                    navController = navController,
                                    viewModel = settingsViewModel,
                                    onStartImageCrop = startImageCrop
                                )
                            }
                            composable("stats"){
                                StatsScreen(
                                    navController = navController,
                                )
                            }
                            composable("recently"){
                                RecentlyScreen(
                                    navController = navController,
                                    viewModel = authViewModel
                                )
                            }
                        }
                    }
                }

            }
        }
    }
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    fun startCropActivityForResult(sourceUri: Uri,  callback: (Uri?) -> Unit) {
        this.currentCropCallback = callback
        val destinationFileName = "cropped_avatar_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))


        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            withAspectRatio(1f, 1f)
            withMaxResultSize(500, 500)
        }

        val uCropIntent = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)
            .getIntent(this)
            uCropLauncher.launch(uCropIntent)
    }
}
