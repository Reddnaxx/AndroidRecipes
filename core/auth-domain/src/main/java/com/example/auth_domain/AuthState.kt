package com.example.auth_domain

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val uid: String, val email: String?) : AuthState()
    data class Error(val message: String) : AuthState()
}