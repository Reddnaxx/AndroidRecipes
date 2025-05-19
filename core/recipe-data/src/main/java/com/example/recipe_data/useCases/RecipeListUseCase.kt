package com.example.recipe_data.useCases

import com.example.recipe_data.mappers.toDomain
import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.IRecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeListUseCase @Inject constructor(
    private val recipeRepository: IRecipeRepository
) {
    suspend fun getRecipes(): List<Recipe> {
        return recipeRepository.getRecipes().map { it.toDomain() }
    }
}