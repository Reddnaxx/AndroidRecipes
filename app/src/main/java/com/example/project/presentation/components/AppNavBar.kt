package com.example.project.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.configs.navigation.Routes

@Composable
fun AppNavBar(
    navController: NavHostController
) {

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Routes.bottomNavItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                onClick = { navController.navigate(it.route) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(it.label) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    }
}
