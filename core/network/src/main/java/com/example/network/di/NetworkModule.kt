package com.example.network.di

import android.content.Context
import com.example.network.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitor(context)
}