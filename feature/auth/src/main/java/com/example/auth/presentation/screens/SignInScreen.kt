package com.example.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth.domain.models.AuthState
import com.example.auth.presentation.viewModels.AuthViewModel
import com.example.theme.Spacing

@Composable
fun SignInScreen(
    onSignIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val error = state.takeIf { it is AuthState.Error }
        ?.let { (it as AuthState.Error).message }

    if (state is AuthState.Authenticated) {
        onSignIn()
    }

    Scaffold { padding ->
        SignInContent(
            modifier = Modifier.padding(padding),
            isLoading = state is AuthState.Loading,
            error = error,
            onGoogleSignInClick = {
                viewModel.signIn()
            })
    }
}

@Composable
private fun SignInContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    onGoogleSignInClick: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Sign In")

                Spacer(modifier = Modifier.height(Spacing.medium))

                Column {
                    Button(
                        onClick = { onGoogleSignInClick() },
                        enabled = isLoading.not(),
                    ) {
                        Text(text = "Sign In with Google")
                    }
                }
            }
        }

        error?.let {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                modifier = Modifier
                    .align(Alignment.BottomCenter),
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}