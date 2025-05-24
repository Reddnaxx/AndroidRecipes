package com.example.recipe_data.repositories

import com.example.recipe_data.dataSources.RecipesDataSource
import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.RecipeRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipesApi: RecipesDataSource,
) : RecipeRepository {

    val uid
        get() = Firebase.auth.uid
            ?: throw IllegalStateException("User not authenticated")

    override suspend fun getRecipes(authorId: String?): List<Recipe> =
        recipesApi.getRecipes(uid, authorId)

    override suspend fun getRecipeById(id: String): Recipe? =
        recipesApi.getRecipeById(id)

    override suspend fun createRecipe(dto: RecipeDto) =
        recipesApi.createRecipe(dto)

    override suspend fun updateRecipe(
        recipeId: String,
        dto: RecipeUpdateDto
    ) = recipesApi.updateRecipe(recipeId, dto)

    override suspend fun deleteRecipe(id: String) =
        recipesApi.deleteRecipe(id)

    override suspend fun getUserFavorites(): List<Recipe> =
        recipesApi.getUserFavorites(uid)

    override suspend fun addToFavorites(recipeId: String) =
        recipesApi.addRecipeToFavorites(uid, recipeId)

    override suspend fun removeFromFavorites(recipeId: String) =
        recipesApi.removeRecipeFromFavorites(uid, recipeId)
}