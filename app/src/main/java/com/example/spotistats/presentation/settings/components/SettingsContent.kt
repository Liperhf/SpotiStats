package com.example.spotistats.presentation.settings.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.example.spotistats.R

@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    isAuthenticated: State<Boolean>,
    onAccountClick:() -> Unit,
    onLanguageClick:() -> Unit

){
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        if (isAuthenticated.value) {
            item{
                SettingsItem(title = stringResource(R.string.account),
                    icon = Icons.Default.AccountCircle,
                    onClick = onAccountClick
                )
            }
            item{
                SettingsItem(title = stringResource(R.string.language),
                    icon = ImageVector.vectorResource(R.drawable.language_24px),
                    onClick = onLanguageClick
                )
            }
        }
    }
}