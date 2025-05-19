package com.example.recipes.di

import com.example.recipes.data.dataSources.RecipesDataSource
import com.example.recipes.data.repositories.RecipeRepository
import com.example.recipes.data.useCases.RecipeListUseCase
import com.example.recipes.domain.repositories.IRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecipesModule {

    @Provides
    fun provideRecipeApi(): RecipesDataSource {
        return RecipesDataSource()
    }

    @Provides
    fun provideRecipeRepository(
        recipesApi: RecipesDataSource
    ): IRecipeRepository {
        return RecipeRepository(recipesApi)
    }

    @Provides
    fun provideRecipeListUseCase(
        recipeRepository: IRecipeRepository
    ): RecipeListUseCase {
        return RecipeListUseCase(recipeRepository)
    }
}