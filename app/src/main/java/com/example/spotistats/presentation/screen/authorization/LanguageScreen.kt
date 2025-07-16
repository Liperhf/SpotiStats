package com.example.spotistats.presentation.screen.authorization

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import java.util.Locale
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spotistats.R
import com.example.spotistats.domain.model.AppLanguage
import com.example.spotistats.util.LanguagePrefs
import com.example.spotistats.util.UpdateLocale
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    navController: NavController,
    viewModel: SettingsViewModel
){
    val systemUiController = rememberSystemUiController()
    val navBarColor = MaterialTheme.colorScheme.background
    val statusBarColor = MaterialTheme.colorScheme.primary
    val context = LocalContext.current

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
            title = {Text(text = stringResource(R.string.language))},
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
        ) }
    ) {paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)){
            item{
                LanguageListItem(title = stringResource(R.string.english,), onClick = {
                    LanguagePrefs.saveLanguage(context,"en")
                    viewModel.setLanguage(AppLanguage.ENGLISH)
                    UpdateLocale(context,Locale("en"))
                    (context as? Activity)?.recreate()
                })
            }
            item { LanguageListItem(title = stringResource(R.string.russian), onClick = {
                LanguagePrefs.saveLanguage(context,"ru")
                viewModel.setLanguage(AppLanguage.RUSSIAN)
                UpdateLocale(context,Locale("ru"))
                (context as? Activity)?.recreate()
            })}
        }
    }
}


@Composable
fun LanguageListItem(title:String,onClick:() -> Unit){
    ListItem(
        headlineContent = {Text(title, fontSize = 18.sp)},
        modifier = Modifier.fillMaxWidth()
            .clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground)
    )
}