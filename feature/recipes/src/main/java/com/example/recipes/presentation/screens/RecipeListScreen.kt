@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)

package com.example.recipes.presentation.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipes.R
import com.example.recipes.presentation.components.RecipeList
import com.example.recipes.presentation.viewModels.RecipeListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    onNavigateToDetails: (id: String) -> Unit,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = viewModel::refreshRecipes,
        modifier = Modifier.fillMaxSize()
    ) {
        RecipeList(
            recipes = recipes,
            isLoading = isLoading,
            onNavigateToDetails = onNavigateToDetails,
            onFavoriteClick = {
                if (it.isFavorite) {
                    viewModel.removeFromFavorites(it.id)
                } else {
                    viewModel.addToFavorites(it.id)
                }
            },
            emptyContent = {
                Text(
                    text = stringResource(R.string.recipe_list_not_found),
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}
