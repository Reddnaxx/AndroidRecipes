package com.example.recipe_domain.repository

import com.example.recipe_domain.dto.RecipeDto

interface IRecipeRepository {
    suspend fun getRecipes(authorId: String? = null): List<RecipeDto>
    suspend fun getRecipeById(id: String): RecipeDto?
    suspend fun addRecipe(recipe: RecipeDto)
    suspend fun updateRecipe(recipe: RecipeDto)
    suspend fun deleteRecipe(id: String)
}