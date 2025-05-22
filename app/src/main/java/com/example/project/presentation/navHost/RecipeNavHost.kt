package com.example.project.presentation.navHost

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.configs.navigation.Routes
import com.example.profile.presentation.screens.ProfileScreen
import com.example.recipes.presentation.screens.RecipeCreationScreen
import com.example.recipes.presentation.screens.RecipeDetailsScreen
import com.example.recipes.presentation.screens.RecipeEditScreen
import com.example.recipes.presentation.screens.RecipeFavoritesScreen
import com.example.recipes.presentation.screens.RecipeListScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RecipeNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it })
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it })
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it })
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it })
        }
    ) {
        composable(route = Routes.LIST) {
            RecipeListScreen(
                onNavigateToDetails = {
                    navController.navigate("${Routes.DETAIL}/${it}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.DETAIL}/{recipeId}",
            arguments = listOf(navArgument("recipeId") {
                type = NavType.StringType
            }),
        ) { entry ->
            RecipeDetailsScreen(
                recipeId = entry.arguments?.getString("recipeId"),
                onNotFound = {
                    navController.navigate(
                        Routes.LIST
                    ) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToEdit = {
                    navController.navigate("${Routes.EDIT}/${it}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }

        composable(route = Routes.FAVORITES) {
            RecipeFavoritesScreen()
        }

        composable(route = Routes.CREATION) {
            RecipeCreationScreen(
                onComplete = {
                    navController.navigate(Routes.PROFILE) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(
            route = "${Routes.EDIT}/{recipeId}",
            arguments = listOf(navArgument("recipeId") {
                type = NavType.StringType
            }),
        ) {
            RecipeEditScreen()
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen(
                onNavigateToDetails = {
                    navController.navigate("${Routes.DETAIL}/${it}") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}