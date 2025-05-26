package com.example.recipe_data.useCases

import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeListUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend fun getRecipes(query: String? = null): List<Recipe> =
        recipeRepository.getRecipes(query = query)
}