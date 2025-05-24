package com.example.recipes.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.recipe_domain.models.Recipe
import com.example.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeList(
    recipes: List<Recipe>,
    isLoading: Boolean,
    onNavigateToDetails: (id: String) -> Unit,
    onFavoriteClick: (recipe: Recipe) -> Unit,
    emptyContent: @Composable () -> Unit = {},
) {
    Crossfade(targetState = isLoading) { loading ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.small),
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            if (loading) {
                items(count = 5) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)),
                        exit = fadeOut()
                    ) {
                        RecipeCardSkeleton()
                    }
                }
            } else if (recipes.isNotEmpty()) {
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
                            animationSpec = tween(300)
                        ),
                        exit = fadeOut()
                    ) {
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onNavigateToDetails(recipe.id) },
                            onFavoriteClick = {
                                onFavoriteClick(recipe)
                            }
                        )
                    }
                }
            } else {
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300)),
                        exit = fadeOut()
                    ) {
                        emptyContent()
                    }
                }
            }
        }
    }
}
