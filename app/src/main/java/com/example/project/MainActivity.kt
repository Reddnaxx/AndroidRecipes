@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.auth.presentation.viewModels.AuthViewModel
import com.example.auth_domain.AuthState
import com.example.configs.navigation.Routes
import com.example.project.presentation.components.AppNavBar
import com.example.project.presentation.navHost.AuthNavHost
import com.example.project.presentation.navHost.RecipeNavHost
import com.example.theme.ProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                val authRepo = hiltViewModel<AuthViewModel>()
                val authState by authRepo.state.collectAsState()

                val currentTitle = when (currentRoute) {
                    Routes.FAVORITES -> "Избранное"
                    Routes.PROFILE -> "Профиль"
                    else -> "Рецепты"
                }

                Scaffold(
                    topBar = {
                        if (authState is AuthState.Authenticated) {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                title = { Text(currentTitle) },
                                navigationIcon = {
                                    if (currentRoute != Routes.LIST) {
                                        BackButton(
                                            onBack = navController::navigateUp
                                        )
                                    }
                                },
                                actions = {
                                    ProfileButton(onClick = {
                                        navController.navigate(Routes.PROFILE) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    })
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (authState is AuthState.Authenticated) {
                            AppNavBar(navController)
                        }
                    }
                ) { padding ->
                    if (authState is AuthState.Authenticated == false) {
                        return@Scaffold AuthNavHost(navController = navController)
                    }

                    Box(modifier = Modifier.padding(padding)) {
                        RecipeNavHost(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileButton(
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier.size(32.dp)
        )
    }
}


@Composable
private fun BackButton(
    onBack: () -> Unit
) {
    IconButton(
        onClick = onBack
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier.size(32.dp)
        )
    }
}
