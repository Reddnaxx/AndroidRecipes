package com.example.recipe_data.repositories

import com.example.recipe_data.dataSources.RecipesDataSource
import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.repository.IRecipeRepository
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipesApi: RecipesDataSource,
) : IRecipeRepository {

    override suspend fun getRecipes(authorId: String?): List<RecipeDto> {
        return recipesApi.getRecipes(authorId)
    }

    override suspend fun getRecipeById(id: String): RecipeDto? {
        return recipesApi.getRecipeById(id)
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