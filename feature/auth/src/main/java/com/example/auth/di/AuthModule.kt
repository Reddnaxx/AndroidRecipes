package com.example.auth.di

import android.content.Context
import com.example.auth.presentation.viewModels.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthViewModel(
        @ApplicationContext context: Context
    ): AuthViewModel {
        return AuthViewModel(context)
    }
}