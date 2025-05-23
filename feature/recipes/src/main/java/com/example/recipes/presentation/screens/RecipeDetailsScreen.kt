@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recipes.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
    onNavigateToEdit: (String) -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    if (recipeId.isNullOrEmpty()) return onNotFound()

    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isOwner by viewModel.isOwner.collectAsState()

    if (state == null && isLoading.not()) {
        viewModel.initialize(recipeId)
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize()
    ) {
        state?.let {
            RecipeDetailContent(
                recipe = it,
                modifier = Modifier.fillMaxSize(),
                isOwner = isOwner,
                onEditClick = { onNavigateToEdit(recipeId) }
            )
        } ?: RecipeDetailSkeleton()
    }
}

@Composable
private fun RecipeDetailContent(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
    onEditClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.medium)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.headlineMedium
            )

            // Edit button
            if (isOwner) {
                IconButton(
                    onClick = onEditClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Image
        AsyncImage(
            model = recipe.imageUrl,
            contentDescription = recipe.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 160.dp, max = 240.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Description
        Text(
            text = recipe.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        // Ingredients title
        Text(
            text = "Ингредиенты",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        // Ingredients list
        recipe.ingredients.forEach {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Instructions title
        Text(
            text = "Приготовление",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(Spacing.small))

        // Instructions list
        recipe.instructions.forEachIndexed { idx, it ->
            Text(
                text = "Шаг ${idx + 1} - $it",
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