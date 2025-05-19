package com.example.recipes.data.useCases

import com.example.recipes.data.mappers.toDomain
import com.example.recipes.data.mappers.toDto
import com.example.recipes.domain.models.Recipe
import com.example.recipes.domain.repositories.IRecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeListUseCase @Inject constructor(
    private val recipeRepository: IRecipeRepository
) {
    suspend fun getRecipes(): List<Recipe> {
        return recipeRepository.getRecipes().map { it.toDomain() }
    }

    suspend fun addRecipe(recipe: Recipe) {
        return recipeRepository.addRecipe(recipe.toDto())
    }
}