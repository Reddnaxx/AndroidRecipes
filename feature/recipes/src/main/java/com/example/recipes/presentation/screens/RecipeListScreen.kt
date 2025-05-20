@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)

package com.example.recipes.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipes.presentation.components.RecipeCard
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
        onRefresh = { viewModel.refreshRecipes() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(recipes) { idx, recipe ->
                val visibleState = remember {
                    MutableTransitionState(false).apply {
                        targetState = true
                    }
                }

                AnimatedVisibility(
                    visibleState = visibleState,
                    enter = fadeIn(
                        initialAlpha = 0.3f,
                        animationSpec = tween(durationMillis = 300)
                    ) + slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(
                            durationMillis = 600,
                            delayMillis = idx * 100
                        )
                    ),
                    exit = fadeOut()
                ) {
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onNavigateToDetails(recipe.id) }
                    )
                }
            }
        }
    }
}
