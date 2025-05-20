@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.profile.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.profile.presentation.components.MyRecipeCard
import com.example.profile.presentation.viewModels.ProfileViewModel
import com.example.theme.Spacing
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val user = viewModel.user!!
    val recipes by viewModel.recipes.collectAsState()

    val scrollState = rememberScrollState(0)

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = Spacing.small)
            .padding(top = Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
    ) {
        item {
            UserInfo(
                user = user,
                onSignOutClick = viewModel::signOut
            )
            Spacer(modifier = Modifier.height(Spacing.large))
        }

        item {
            Text(
                text = "Мои рецепты",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        items(recipes, key = { it.id }) {
            MyRecipeCard(recipe = it, onNavigateToEdit = {})
        }
    }
}

@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    user: FirebaseUser,
    onSignOutClick: () -> Unit = {},
) {
    val isNameEmpty = user.displayName.isNullOrEmpty()

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user.photoUrl ?: "https://i.pravatar.cc/300",
            contentDescription = "User Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "${if (isNameEmpty) "Аноним" else user.displayName}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${user.email}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(0.75f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Out Button
        IconButton(
            onClick = onSignOutClick,
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ExitToApp,
                contentDescription = "Sign Out",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}