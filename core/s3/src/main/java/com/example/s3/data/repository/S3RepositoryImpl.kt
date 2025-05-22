package com.example.s3.data.repository

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.s3.domain.repository.S3Repository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class S3RepositoryImpl @Inject constructor(
    private val s3Client: AmazonS3Client
) : S3Repository {

    companion object {
        private const val BASE_URL = "https://storage.yandexcloud.net"
        private const val BUCKET_NAME = "rukavishnikov-recipes"
    }

    override suspend fun uploadFile(
        filePath: String,
        objectKey: String
    ): String {
        val file = File(filePath).also {
            require(it.exists() && it.isFile) {
                "File not found or is not a file: $filePath"
            }
        }

        val putRequest = PutObjectRequest(
            BUCKET_NAME,
            objectKey,
            file
        )

        s3Client.putObject(putRequest)

        return "$BASE_URL/$BUCKET_NAME/$objectKey"
    }

    override suspend fun getFileUrl(
        objectKey: String
    ): String {
        return "$BASE_URL/$BUCKET_NAME/$objectKey"
    }
}
