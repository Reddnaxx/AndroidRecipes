package com.example.project.presentation.navHost

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.auth.presentation.screens.SignInScreen
import com.example.configs.navigation.Routes
import com.example.profile.presentation.screens.ProfileScreen
import com.example.recipes.presentation.screens.RecipeDetailsScreen
import com.example.recipes.presentation.screens.RecipeFavoritesScreen
import com.example.recipes.presentation.screens.RecipeListScreen

@Composable
fun RecipeNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SIGN_IN,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // сверху: справа
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth }, // справа ←←←
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth }, // уходит налево
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        composable(route = Routes.LIST) {
            RecipeListScreen(
                onNavigateToDetails = { navController.navigate("${Routes.DETAIL}/${it}") }
            )
        }

        composable(
            route = "${Routes.DETAIL}/{recipeId}",
            arguments = listOf(navArgument("recipeId") {
                type = NavType.StringType
            })
        ) {
            RecipeDetailsScreen()
        }

        composable(route = Routes.FAVORITES) {
            RecipeFavoritesScreen()
        }

        composable(route = Routes.PROFILE) {
            ProfileScreen()
        }

        composable(route = Routes.SIGN_IN) {
            SignInScreen(onSignIn = {
                navController.navigate(Routes.LIST)
            })
        }
    }
}