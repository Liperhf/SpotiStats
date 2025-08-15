package com.example.spotistats.presentation.common

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.spotistats.R

data class BottomNavItem(
    val labelResId: Int,
    val icon: ImageVector,
    val route: String,
)

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(
            labelResId = R.string.home,
            icon = Icons.Default.Home,
            route = "main"
        ),
        BottomNavItem(
            labelResId = R.string.stats,
            icon = ImageVector.vectorResource(R.drawable.monitoring_24px),
            route = "stats"
        ),
    )
    val homeAssociatedRoutes = setOf("main", "settings", "account", "language", "recently")

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.height(65.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val isSelected = if (item.route == "main") {
                currentRoute in homeAssociatedRoutes
            } else {
                currentRoute == item.route
            }
            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = stringResource(item.labelResId),
                        modifier = Modifier.size(30.dp)
                    )
                },
                label = { Text(stringResource(item.labelResId)) },
                onClick = {
                    if (item.route == "main") {
                        if (currentRoute != "main") {
                            navController.navigate("main") {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true


                            }
                        }
                    } else {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}