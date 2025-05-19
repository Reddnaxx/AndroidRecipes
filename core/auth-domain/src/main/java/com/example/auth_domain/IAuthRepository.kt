package com.example.auth_domain

import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun authState(): Flow<AuthState>

    suspend fun signUp(email: String, password: String)
    suspend fun signIn(email: String, password: String)
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signOut()
    suspend fun setError(message: String)
}