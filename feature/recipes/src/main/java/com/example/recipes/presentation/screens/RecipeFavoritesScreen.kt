package com.example.recipes.presentation.screens

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
import com.example.recipes.presentation.viewModels.RecipeFavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFavoritesScreen(
    onNavigateToDetails: (id: String) -> Unit,
    viewModel: RecipeFavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize()
    ) {
        RecipeList(
            recipes = favorites,
            isLoading = isLoading,
            onNavigateToDetails = onNavigateToDetails,
            onFavoriteClick = {
                viewModel.removeFromFavorites(it.id)
            },
            emptyContent = {
                Text(
                    text = stringResource(R.string.favorites_empty),
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}