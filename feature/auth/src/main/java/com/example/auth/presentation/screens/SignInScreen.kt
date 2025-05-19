package com.example.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth.R
import com.example.auth.presentation.viewModels.AuthViewModel
import com.example.auth_domain.AuthState
import com.example.theme.Spacing
import com.example.ui.controls.input.presentation.componetns.ValidatingInputTextField
import com.example.ui.controls.input.presentation.viewModels.EmailViewModel
import com.example.ui.controls.input.presentation.viewModels.PasswordViewModel

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
) {

    val state by viewModel.state.collectAsState()
    val error = state.takeIf { it is AuthState.Error }
        ?.let { (it as AuthState.Error).message }

    SignUpContent(
        isLoading = state is AuthState.Loading,
        error = error,
        onGoogleSignInClick = viewModel::signInWithGoogle,
        onSignInClick = viewModel::signIn,
        onToSignUpClick = onNavigateToSignUp,
        modifier = Modifier.imePadding()
    )
}

@Composable
private fun SignUpContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null,
    onGoogleSignInClick: () -> Unit,
    onSignInClick: (email: String, password: String) -> Unit,
    onToSignUpClick: () -> Unit = {},
) {
    var emailViewModel = hiltViewModel<EmailViewModel>()
    val passwordViewModel = hiltViewModel<PasswordViewModel>()

    val isValid = passwordViewModel.passwordHasErrors.not() &&
            emailViewModel.emailHasErrors.not() &&
            emailViewModel.email.isNotBlank() &&
            passwordViewModel.password.isNotBlank()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier
                    .padding(Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Title Text
                Text(
                    text = "Вход",
                    style = MaterialTheme.typography.headlineMedium,
                )

                Spacer(modifier = Modifier.height(Spacing.medium))

                // Email Field
                ValidatingInputTextField(
                    label = "Email",
                    disabled = isLoading,
                    value = emailViewModel.email,
                    onValueChange = emailViewModel::updateEmail,
                    errorMessage = "Некорректный email",
                    validatorHasErrors = emailViewModel.emailHasErrors,
                    keyBoardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(Spacing.extraSmall))

                // Password Field
                ValidatingInputTextField(
                    label = "Пароль",
                    isPassword = true,
                    disabled = isLoading,
                    value = passwordViewModel.password,
                    onValueChange = passwordViewModel::updatePassword,
                    errorMessage = "Пароль должен содержать минимум 6 символов",
                    validatorHasErrors = passwordViewModel.passwordHasErrors,
                    keyBoardType = KeyboardType.Password
                )

                Spacer(modifier = Modifier.height(Spacing.medium))

                // Sign In Button
                Button(
                    onClick = {
                        if (isValid) {
                            onSignInClick(
                                emailViewModel.email,
                                passwordViewModel.password
                            )
                        }
                    },
                    enabled = isLoading.not() && isValid,
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.medium))

                // Google Sign In Button
                Button(
                    onClick = { onGoogleSignInClick() },
                    enabled = isLoading.not(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = Spacing.medium)
                            .size(32.dp)
                    )
                    Text(
                        text = "Войти через Google",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Navigate To Sign Up Button
            TextButton(
                onClick = onToSignUpClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Нет аккаунта? Зарегистрируйтесь",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
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