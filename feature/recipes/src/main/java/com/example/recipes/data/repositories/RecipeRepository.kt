package com.example.recipes.data.repositories

import com.example.recipes.data.dataSources.RecipesDataSource
import com.example.recipes.data.dto.RecipeDto
import com.example.recipes.domain.repositories.IRecipeRepository
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipesApi: RecipesDataSource,
) : IRecipeRepository {
    override suspend fun getRecipes(): List<RecipeDto> {
        return recipesApi.getRecipes()
    }

    override suspend fun getRecipeById(id: String): RecipeDto? {
        TODO("Not yet implemented")
    }

    override suspend fun addRecipe(recipe: RecipeDto) {
        return recipesApi.addRecipe(recipe)
    }

    override suspend fun updateRecipe(recipe: RecipeDto) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(id: String) {
        TODO("Not yet implemented")
    }

}