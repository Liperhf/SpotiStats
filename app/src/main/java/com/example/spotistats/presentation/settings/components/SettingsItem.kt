package com.example.spotistats.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotistats.ui.theme.SpotiStatsTheme

@Composable
fun SettingsItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontSize = 18.sp) },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(35.dp)
            )
        },
        modifier = Modifier
            .clickable { onClick() }
            .height(70.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            leadingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview(name = "SettingsItem", showBackground = true)
@Composable
private fun SettingsItemPreview() {
    SpotiStatsTheme {
        SettingsItem(title = "Настройки", icon = Icons.Default.Settings, onClick = {})
    }
}