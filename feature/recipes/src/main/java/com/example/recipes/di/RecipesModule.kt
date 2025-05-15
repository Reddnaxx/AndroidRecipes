package com.example.recipes.di

import com.example.recipes.data.dataSources.RecipesDataSource
import com.example.recipes.data.repositories.RecipeRepository
import com.example.recipes.data.useCases.RecipeListUseCase
import com.example.recipes.domain.repositories.IRecipeRepository
import com.example.recipes.presentation.viewModels.RecipeListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
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
    fun provideRecipeViewModel(
        recipeListUseCase: RecipeListUseCase
    ): RecipeListViewModel {
        return RecipeListViewModel(recipeListUseCase)
    }
}