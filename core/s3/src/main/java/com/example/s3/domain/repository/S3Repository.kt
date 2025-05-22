package com.example.s3.domain.repository

interface S3Repository {
    suspend fun uploadFile(
        filePath: String,
        objectKey: String
    ): String

    suspend fun getFileUrl(
        objectKey: String
    ): String
}