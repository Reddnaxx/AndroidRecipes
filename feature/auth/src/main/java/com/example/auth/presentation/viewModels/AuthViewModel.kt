package com.example.auth.presentation.viewModels

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.BuildConfig
import com.example.auth_domain.AuthState
import com.example.auth_domain.IAuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: IAuthRepository,
) : ViewModel() {

    private val credentialManager = CredentialManager.create(context)

    val state: StateFlow<AuthState> = repo.authState().stateIn(
        viewModelScope, SharingStarted.Lazily, AuthState.Unauthenticated
    )

    fun signUp(
        email: String,
        password: String
    ) = viewModelScope.launch {
        repo.signUp(email, password)
    }

    fun signIn(
        email: String,
        password: String
    ) = viewModelScope.launch {
        repo.signIn(email, password)
    }

    fun signInWithGoogle() = viewModelScope.launch {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            val googleIdTokenCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            repo.signInWithGoogle(googleIdTokenCredential.idToken)
        } catch (e: NoCredentialException) {
            Log.d("AuthViewModel", "NoCredentialException: ${e.message}")
            repo.setError("${e.message}")
        } catch (e: GetCredentialCancellationException) {
            Log.d(
                "AuthViewModel",
                "GetCredentialCancellationException: ${e.message}"
            )
        } catch (e: Exception) {
            Log.d("AuthViewModel", "Exception: ${e.message}")
            repo.setError("${e.message}")
        }
    }
}