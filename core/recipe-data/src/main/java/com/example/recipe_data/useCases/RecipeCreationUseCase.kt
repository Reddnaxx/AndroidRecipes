package com.example.recipe_data.useCases

import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeCreationUseCase @Inject constructor(
    private val repo: RecipeRepository
) {

    suspend fun createRecipe(recipe: RecipeDto) = repo.createRecipe(recipe)
}