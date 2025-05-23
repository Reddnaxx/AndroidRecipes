package com.example.recipe_data.useCases

import com.example.recipe_data.mappers.toDomain
import com.example.recipe_data.mappers.toDto
import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeProfileUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {

    suspend fun getUserRecipes(authorId: String): List<Recipe> =
        recipeRepository.getRecipes(authorId).map { it.toDomain() }

    suspend fun addRecipe(recipe: Recipe) =
        recipeRepository.createRecipe(recipe.toDto())
}