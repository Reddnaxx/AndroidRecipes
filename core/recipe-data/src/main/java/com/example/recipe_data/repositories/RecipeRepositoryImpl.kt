package com.example.recipe_data.repositories

import com.example.recipe_data.dataSources.RecipesDataSource
import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipesApi: RecipesDataSource,
) : RecipeRepository {

    override suspend fun getRecipes(authorId: String?): List<RecipeDto> =
        recipesApi.getRecipes(authorId)

    override suspend fun getRecipeById(id: String): RecipeDto? =
        recipesApi.getRecipeById(id)

    override suspend fun createRecipe(recipe: RecipeDto) =
        recipesApi.createRecipe(recipe)

    override suspend fun updateRecipe(
        recipeId: String,
        dto: RecipeUpdateDto
    ) = recipesApi.updateRecipe(recipeId, dto)

    override suspend fun deleteRecipe(id: String) =
        recipesApi.deleteRecipe(id)

}