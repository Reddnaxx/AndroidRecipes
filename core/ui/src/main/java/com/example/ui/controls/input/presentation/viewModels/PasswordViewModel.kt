package com.example.ui.controls.input.presentation.viewModels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class PasswordViewModel @Inject constructor() : ViewModel() {
    var password by mutableStateOf("")
        private set

    val passwordHasErrors by derivedStateOf {
        if (password.isNotEmpty()) {
            password.length < 6
        } else {
            false
        }
    }

    fun updatePassword(input: String) {
        password = input
    }
}