package com.example.recipe_data.useCases

import com.example.recipe_domain.repository.RecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeFavoritesUseCase @Inject constructor(
    private val repo: RecipeRepository
) {

    suspend fun getUserFavorites() = repo.getUserFavorites()

    suspend fun addRecipeToFavorites(recipeId: String) =
        repo.addToFavorites(recipeId)

    suspend fun removeRecipeFromFavorites(recipeId: String) =
        repo.removeFromFavorites(recipeId)
}