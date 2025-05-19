package com.example.auth_data.di

import android.content.Context
import com.example.auth_data.repository.AuthRepository
import com.example.auth_domain.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthDataModule {

    @Provides
    fun provideAuthRepository(
        @ApplicationContext context: Context
    ): IAuthRepository {
        return AuthRepository(context)
    }
}