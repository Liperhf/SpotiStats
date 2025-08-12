package com.example.spotistats.presentation.settings.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotistats.R
import com.example.spotistats.presentation.auth.AuthUiState
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    onAccountClick: () -> Unit,
    onLanguageClick: () -> Unit,
    uiState: AuthUiState

) {
    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        if (uiState.isAuthenticated) {
            item {
                SettingsItem(
                    title = stringResource(R.string.account),
                    icon = Icons.Default.AccountCircle,
                    onClick = onAccountClick
                )
            }
            item {
                SettingsItem(
                    title = stringResource(R.string.language),
                    icon = ImageVector.vectorResource(R.drawable.language_24px),
                    onClick = onLanguageClick
                )
            }
        }
    }
}

@Preview(name = "SettingsContent", showBackground = true)
@Composable
private fun SettingsContentPreview() {
    SpotiStatsTheme {
        SettingsContent(
            paddingValues = PaddingValues(),
            onAccountClick = {},
            onLanguageClick = {},
            uiState = AuthUiState(isAuthenticated = true)
        )
    }
}