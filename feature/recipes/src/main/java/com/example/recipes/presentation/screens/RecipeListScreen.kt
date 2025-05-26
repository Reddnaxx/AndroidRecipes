@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)

package com.example.recipes.presentation.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
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
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState(true)
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val query by viewModel.query.collectAsState()

    Column {

        // Search field
        TextField(
            value = query,
            onValueChange = viewModel::onQueryChanged,
            label = { Text(text = stringResource(R.string.recipe_list_search)) },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            shape = RectangleShape,
            suffix = {
                TextButton(
                    onClick = {
                        viewModel.onQueryChanged("")
                    },
                    enabled = query.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(R.string.recipe_list_clear_search),
                        modifier = Modifier.clickable {
                            viewModel.onQueryChanged("")
                        }
                    )
                }
            }
        )

        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = viewModel::refreshRecipes,
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            RecipeList(
                recipes = recipes,
                isLoading = isLoading,
                isNetworkAvailable = isNetworkAvailable,
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
}
