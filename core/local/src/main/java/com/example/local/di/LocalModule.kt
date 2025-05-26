package com.example.local.di

import android.content.Context
import com.example.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.buildDatabase(context)
}