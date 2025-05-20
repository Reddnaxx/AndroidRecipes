package com.example.recipe_data.useCases

import com.example.recipe_data.mappers.toDomain
import com.example.recipe_domain.models.Recipe
import com.example.recipe_domain.repository.IRecipeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeDetailUseCase @Inject constructor(
    private val recipeRepository: IRecipeRepository
) {
    suspend fun getRecipeById(id: String): Recipe? {
        return recipeRepository.getRecipeById(id)?.toDomain()
    }
}