@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.profile.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.profile.presentation.viewModels.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    Button(
        onClick = {
            viewModel.signOut()
        }
    ) {
        Text(text = "Sign Out")
    }
}