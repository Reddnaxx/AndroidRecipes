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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.configs.navigation.Routes
import com.example.project.presentation.components.AppNavBar
import com.example.project.presentation.navHost.RecipeNavHost
import com.example.project.ui.theme.ProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            title = { Text("Recipe App") },
                            actions = {
                                IconButton(
                                    onClick = { navController.navigate(Routes.PROFILE) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Profile",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        AppNavBar(navController)
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        RecipeNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
