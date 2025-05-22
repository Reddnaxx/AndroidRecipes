package com.example.s3.di

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.example.s3.BuildConfig
import com.example.s3.data.repository.S3RepositoryImpl
import com.example.s3.domain.repository.S3Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object S3Module {

    @Provides
    @Singleton
    fun provideAWSCredentials(): BasicAWSCredentials {
        return BasicAWSCredentials(
            BuildConfig.ACCESS_KEY_ID,
            BuildConfig.SECRET_ACCESS_KEY
        )
    }

    @Provides
    @Singleton
    fun provideS3Client(
        credentials: BasicAWSCredentials
    ): AmazonS3Client {
        return AmazonS3Client(
            credentials,
            Region.getRegion("us-east-1")
        ).apply {
            setEndpoint("https://storage.yandexcloud.net")
            setSignerRegionOverride("ru-central1")
        }
    }

    @Provides
    @Singleton
    fun provideS3Repository(s3Client: AmazonS3Client): S3Repository =
        S3RepositoryImpl(s3Client)
}
