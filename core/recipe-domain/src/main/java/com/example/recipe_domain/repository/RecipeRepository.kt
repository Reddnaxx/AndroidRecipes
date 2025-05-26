package com.example.recipe_domain.repository

import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.recipe_domain.models.Recipe

interface RecipeRepository {
    suspend fun getRecipes(
        authorId: String? = null,
        query: String? = null
    ): List<Recipe>

    suspend fun getRecipeById(id: String): Recipe?
    suspend fun createRecipe(dto: RecipeDto)
    suspend fun updateRecipe(recipeId: String, dto: RecipeUpdateDto)
    suspend fun deleteRecipe(id: String)
    suspend fun getUserFavorites(): List<Recipe>
    suspend fun addToFavorites(recipeId: String)
    suspend fun removeFromFavorites(recipeId: String)
}