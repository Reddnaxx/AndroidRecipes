package com.example.project.presentation.navHost

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.auth.presentation.screens.SignInScreen
import com.example.auth.presentation.screens.SignUpScreen
import com.example.configs.navigation.Routes

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.SIGN_IN
    ) {
        composable(route = Routes.SIGN_IN) {
            SignInScreen(
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGN_UP) {
                        popUpTo(Routes.SIGN_IN) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }

        composable(route = Routes.SIGN_UP) {
            SignUpScreen(
                onNavigateToSignIn = {
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.SIGN_UP) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}