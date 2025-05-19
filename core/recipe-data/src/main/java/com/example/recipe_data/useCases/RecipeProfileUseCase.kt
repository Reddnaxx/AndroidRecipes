package com.example.recipe_data.useCases

import com.example.recipe_data.mappers.toDomain
import com.example.recipe_data.mappers.toDto
import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.IRecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeProfileUseCase @Inject constructor(
    private val recipeRepository: IRecipeRepository
) {
    suspend fun getUserRecipes(authorId: String): List<Recipe> {
        return recipeRepository.getRecipes(authorId).map { it.toDomain() }
    }

    suspend fun addRecipe(recipe: Recipe) {
        return recipeRepository.addRecipe(recipe.toDto())
    }
}