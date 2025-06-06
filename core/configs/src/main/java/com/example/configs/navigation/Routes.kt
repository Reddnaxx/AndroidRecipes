package com.example.configs.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import com.example.configs.navigation.domain.models.BottomNavItem

object Routes {
    const val LIST = "recipe_list"
    const val DETAIL = "recipe_detail"
    const val FAVORITES = "recipe_favorites"
    const val CREATION = "recipe_creation"
    const val EDIT = "recipe_edit"
    const val PROFILE = "profile"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"

    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Рецепты",
            icon = Icons.Filled.Home,
            route = LIST,
        ),
        BottomNavItem(
            label = "Избранное",
            icon = Icons.Filled.Favorite,
            route = FAVORITES,
        ),
    )
}