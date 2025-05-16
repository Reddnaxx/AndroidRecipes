package com.example.project.di

import com.example.auth.di.AuthModule
import com.example.recipes.di.RecipesModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RecipesModule::class, AuthModule::class])
@InstallIn(SingletonComponent::class)
object AppModule