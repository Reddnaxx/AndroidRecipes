package com.example.auth_data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.NoCredentialException
import com.example.auth_domain.AuthState
import com.example.auth_domain.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : IAuthRepository {
    private val credManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _state = MutableStateFlow<AuthState>(initial())
    override fun authState(): Flow<AuthState> = _state

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _state.value = auth.currentUser
                ?.let { AuthState.Authenticated(it.uid, it.email) }
                ?: AuthState.Unauthenticated
        }
    }

    override suspend fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful.not()) {
                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            _state.value = AuthState.Error(
                                "Пользователь с таким email уже существует"
                            )
                        }

                        else -> {
                            _state.value = AuthState.Error(
                                it.exception?.message ?: "Unknown error"
                            )
                            Log.d("Register", "${it.exception?.message}")
                        }
                    }
                }
            }
    }

    override suspend fun signIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful.not()) {
                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            _state.value = AuthState.Error(
                                "Пользователь не найден"
                            )
                        }

                        else -> {
                            _state.value = AuthState.Error(
                                it.exception?.message ?: "Unknown error"
                            )
                            Log.d("SignIn", "${it.exception?.message}")
                        }
                    }
                }
            }
    }

    override suspend fun signInWithGoogle(idToken: String) {
        try {
            val cred = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(cred).addOnCompleteListener {
                if (it.isSuccessful.not()) {
                    _state.value = AuthState.Error(
                        it.exception?.message ?: "Unknown error"
                    )
                }
            }
        } catch (e: NoCredentialException) {
            Log.d("SignInWithGoogle", "NoCredentialException: ${e.message}")
            _state.value = AuthState.Error(
                "Необходимо авторизоваться в Google"
            )
        } catch (e: Exception) {
            Log.d("SignInWithGoogle", "Exception: ${e.message}")
            _state.value = AuthState.Error(
                "Неизвестная ошибка: ${e.message}"
            )
        }
    }

    override suspend fun signOut() {
        runCatching {
            credManager.clearCredentialState(ClearCredentialStateRequest())
        }
        firebaseAuth.signOut()
    }

    override suspend fun setError(message: String) {
        _state.value = AuthState.Error(message)
    }

    private fun initial(): AuthState = firebaseAuth.currentUser
        ?.let { AuthState.Authenticated(it.uid, it.email) }
        ?: AuthState.Unauthenticated
}