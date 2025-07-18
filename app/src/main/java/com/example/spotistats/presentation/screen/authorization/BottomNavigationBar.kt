package com.example.spotistats.presentation.screen.authorization

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val route:String,
    )

@Composable
fun AppBottomNavigationBar(navController: NavController){
    val items = listOf(
        BottomNavItem(
            labelResId = R.string.home,
            icon = Icons.Default.Home,
            route = "main"
        ),
        BottomNavItem(
            labelResId =R.string.stats,
            icon = ImageVector.vectorResource(R.drawable.monitoring_24px),
            route = "stats"
        ),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.height(50.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = {Icon(item.icon,
                    contentDescription = stringResource(item.labelResId),
                    modifier = Modifier.size(30.dp))},
                label = {item.labelResId},
                onClick = {navController.navigate(item.route){
                    popUpTo(navController.graph.startDestinationId){
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
