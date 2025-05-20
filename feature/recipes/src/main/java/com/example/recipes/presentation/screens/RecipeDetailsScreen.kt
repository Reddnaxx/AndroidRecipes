@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.recipe_domain.models.Recipe
import com.example.recipes.presentation.viewModels.RecipeDetailViewModel
import com.example.theme.Spacing

@Composable
fun RecipeDetailsScreen(
    recipeId: String?,
    onNotFound: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    if (recipeId.isNullOrEmpty()) return onNotFound()

    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (state == null && isLoading.not()) {
        viewModel.initialize(recipeId)
    }
    state?.let {
        RecipeDetailContent(
            recipe = it,
            modifier = Modifier.fillMaxSize()
        )
    } ?: RecipeDetailSkeleton()
}

@Composable
private fun RecipeDetailContent(
    recipe: Recipe,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        Text(
            text = recipe.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        Text(
            text = recipe.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        Text(
            text = "Ингредиенты",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        recipe.ingredients.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(Spacing.medium))

        Text(
            text = "Приготовление",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        recipe.instructions.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun RecipeDetailSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.medium)
    ) {
        // Заголовок
        Spacer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.6f)
                .height(32.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Картинка
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Описание
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // "Ингредиенты"
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(24.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        // Список ингредиентов (3 строки)
        repeat(3) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(Spacing.medium))

        // "Приготовление"
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(24.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        // Список шагов (3 строки)
        repeat(3) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}