package com.example.recipe_data.di

import com.example.recipe_data.dataSources.RecipesDataSource
import com.example.recipe_data.repositories.RecipeRepositoryImpl
import com.example.recipe_data.useCases.RecipeCreationUseCase
import com.example.recipe_data.useCases.RecipeEditUseCase
import com.example.recipe_data.useCases.RecipeListUseCase
import com.example.recipe_data.useCases.RecipeProfileUseCase
import com.example.recipe_domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecipesModule {

    @Provides
    fun provideRecipeApi(): RecipesDataSource = RecipesDataSource()

    @Provides
    fun provideRecipeRepository(
        recipesApi: RecipesDataSource
    ): RecipeRepository = RecipeRepositoryImpl(recipesApi)

    @Provides
    fun provideRecipeListUseCase(
        recipeRepository: RecipeRepository
    ): RecipeListUseCase = RecipeListUseCase(recipeRepository)

    @Provides
    fun provideProfileUseCase(
        recipeRepository: RecipeRepository
    ): RecipeProfileUseCase = RecipeProfileUseCase(recipeRepository)

    @Provides
    fun provideRecipeCreationUseCase(
        recipeRepository: RecipeRepository
    ): RecipeCreationUseCase = RecipeCreationUseCase(recipeRepository)

    @Provides
    fun provideRecipeEditUseCase(
        recipeRepository: RecipeRepository
    ): RecipeEditUseCase = RecipeEditUseCase(recipeRepository)
}