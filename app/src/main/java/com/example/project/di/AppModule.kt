package com.example.project.di

import com.example.recipes.di.RecipesModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RecipesModule::class])
@InstallIn(SingletonComponent::class)
object AppModule