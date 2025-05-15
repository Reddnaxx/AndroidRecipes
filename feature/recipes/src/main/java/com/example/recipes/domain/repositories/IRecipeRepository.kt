package com.example.recipes.domain.repositories

import com.example.recipes.data.dto.RecipeDto

interface IRecipeRepository {
    suspend fun getRecipes(): List<RecipeDto>
    suspend fun getRecipeById(id: String): RecipeDto?
    suspend fun addRecipe(recipe: RecipeDto)
    suspend fun updateRecipe(recipe: RecipeDto)
    suspend fun deleteRecipe(id: String)
}