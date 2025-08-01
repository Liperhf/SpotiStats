package com.example.spotistats.presentation.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.spotistats.R

@Composable
fun DrawerItem(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector
) {
    NavigationDrawerItem(
        label = { Text(text) },
        selected = false,
        onClick = { onClick() },
        modifier = Modifier,
        icon = { Icon(imageVector = icon, contentDescription = stringResource(R.string.icon)) },
        colors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.background,
            unselectedContainerColor = MaterialTheme.colorScheme.background,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}