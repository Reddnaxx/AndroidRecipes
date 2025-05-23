package com.example.recipe_domain.repository

import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto

interface RecipeRepository {
    suspend fun getRecipes(authorId: String? = null): List<RecipeDto>
    suspend fun getRecipeById(id: String): RecipeDto?
    suspend fun createRecipe(recipe: RecipeDto)
    suspend fun updateRecipe(recipeId: String, recipe: RecipeUpdateDto)
    suspend fun deleteRecipe(id: String)
}