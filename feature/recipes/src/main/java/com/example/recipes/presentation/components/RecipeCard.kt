package com.example.recipes.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.recipe_domain.models.Recipe
import com.example.theme.Spacing

@Composable
fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(Spacing.medium)) {

            // Recipe image
            recipe.imageUrl.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = recipe.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 200.dp)
                        .clip(MaterialTheme.shapes.medium)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Row {
                Column(modifier = Modifier.weight(1f)) {

                    // Recipe name
                    Text(
                        text = recipe.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Recipe description
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(Spacing.small))

                // Favorite button
                IconButton(
                    modifier = Modifier.align(Alignment.Bottom),
                    onClick = onFavoriteClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Icon(
                        imageVector = if (recipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeCardSkeleton(
    modifier: Modifier = Modifier
) {
    val placeholderColor =
        MaterialTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.5f)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = {}
    ) {
        Column(modifier = Modifier.padding(Spacing.medium)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp, max = 200.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(placeholderColor)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Column(modifier = Modifier.weight(1f)) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(24.dp)
                            .background(placeholderColor)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                            .background(placeholderColor)
                    )
                }

                Spacer(modifier = Modifier.width(Spacing.small))

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            placeholderColor,
                            shape = MaterialTheme.shapes.small
                        )
                        .align(Alignment.Bottom)
                )
            }
        }
    }
}


@Preview
@Composable
private fun RecipeCardPreview() {
    RecipeCard(
        recipe = Recipe(
            id = "1",
            name = "Sample Recipe",
            description = "This is a sample recipe description.",
            ingredients = listOf("Ingredient 1", "Ingredient 2"),
            instructions = listOf("Step 1", "Step 2"),
            imageUrl = "https://example.com/image.jpg",
            authorId = "id"
        ),
    )
}

@Preview
@Composable
private fun RecipeCardSkeletonPreview() {
    RecipeCardSkeleton()
}