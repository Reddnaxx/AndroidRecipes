package com.example.recipes.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipes.presentation.viewModels.RecipeEditViewModel
import com.example.theme.Spacing
import com.example.ui.controls.input.presentation.componetns.ChipInput
import com.example.ui.controls.input.presentation.componetns.DynamicInputList
import com.example.ui.controls.input.presentation.componetns.ImageSelector

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeEditScreen(
    recipeId: String?,
    viewModel: RecipeEditViewModel = hiltViewModel(),
    onComplete: () -> Unit,
    onNotFound: () -> Unit
) {

    if (recipeId.isNullOrEmpty()) return onNotFound()

    val state = viewModel.state
    val scrollState = rememberScrollState()

    if (state.isLoaded.not() && state.isLoading.not()) {
        viewModel.initialize(recipeId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(Spacing.medium)
    ) {

        // Recipe image input
        ImageSelector(
            imageUri = state.image,
            onTakePhoto = {
                val uri = viewModel.storeBitmapToCache(it)
                viewModel.setImageUri(uri.toString())
            },
            onPickImage = viewModel::setImageUri,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        // Recipe name input
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::setName,
            label = { Text("Название рецепта") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Recipe description input
        OutlinedTextField(
            value = state.description,
            onValueChange = viewModel::setDescription,
            minLines = 2,
            label = { Text("Описание рецепта") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Recipe ingredients input
        ChipInput(
            value = state.ingredients,
            label = "Ингредиенты",
            onAdd = viewModel::addIngredient,
            onDelete = viewModel::removeIngredient
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Recipe steps input
        DynamicInputList(
            value = state.instructions,
            label = "Шаги",
            inputLabel = "Шаг",
            onAdd = viewModel::addStep,
            onValueChange = viewModel::updateStep,
            onRemove = viewModel::removeStep,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        // Update recipe button
        Button(
            onClick = {
                viewModel.updateRecipe(
                    onComplete = onComplete
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = state.isValid && !state.isSubmitting && !state.isDeleting,
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(Spacing.small))

            Text(
                text = "Завершить редактирование",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(Spacing.large))

        // Delete recipe button
        TextButton(
            onClick = {
                viewModel.deleteRecipe(
                    onComplete = onComplete
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = state.isValid && !state.isDeleting && !state.isSubmitting,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            if (state.isDeleting && !state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(Spacing.small))

            Text(
                text = "Удалить рецепт",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


