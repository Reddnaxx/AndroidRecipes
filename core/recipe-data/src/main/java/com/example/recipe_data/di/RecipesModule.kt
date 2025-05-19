package com.example.recipe_data.di

import com.example.recipe_data.dataSources.RecipesDataSource
import com.example.recipe_data.repositories.RecipeRepository
import com.example.recipe_data.useCases.RecipeListUseCase
import com.example.recipe_data.useCases.RecipeProfileUseCase
import com.example.recipe_domain.repository.IRecipeRepository
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

    @Provides
    fun provideProfileUseCase(
        recipeRepository: IRecipeRepository
    ): RecipeProfileUseCase {
        return RecipeProfileUseCase(recipeRepository)
    }
}