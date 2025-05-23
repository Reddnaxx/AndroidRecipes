package com.example.recipe_data.useCases

import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeEditUseCase @Inject constructor(
    private val repo: RecipeRepository
) {

    suspend fun updateRecipe(recipeId: String, dto: RecipeUpdateDto) =
        repo.updateRecipe(recipeId, dto)

    suspend fun deleteRecipe(recipeId: String) = repo.deleteRecipe(recipeId)

    suspend fun getRecipeById(id: String) = repo.getRecipeById(id)
}