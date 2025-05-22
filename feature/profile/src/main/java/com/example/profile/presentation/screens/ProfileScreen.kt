@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.profile.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.profile.R
import com.example.profile.presentation.components.MyRecipeCard
import com.example.profile.presentation.components.MyRecipeCardSkeleton
import com.example.profile.presentation.viewModels.ProfileViewModel
import com.example.theme.Spacing
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToDetails: (String) -> Unit
) {
    val user = viewModel.user!!
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val scrollState = rememberScrollState(0)

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = viewModel::refresh,
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = Spacing.small, vertical = Spacing.large),
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
                    text = stringResource(R.string.profile_my_recipes),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            when {
                isLoading -> {
                    items(2) { idx ->
                        val visibleState = remember {
                            MutableTransitionState(false).apply {
                                targetState = true
                            }
                        }

                        AnimatedVisibility(
                            visibleState = visibleState,
                            enter = fadeIn(
                                initialAlpha = 0.3f,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    delayMillis = idx * 100
                                )
                            ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300))
                        ) {
                            MyRecipeCardSkeleton()
                        }
                    }
                }

                recipes.isNotEmpty() -> {
                    itemsIndexed(
                        items = recipes,
                        key = { _, recipe -> recipe.id }
                    ) { idx, recipe ->
                        val visibleState = remember {
                            MutableTransitionState(false).apply {
                                targetState = true
                            }
                        }

                        AnimatedVisibility(
                            visibleState = visibleState,
                            enter = fadeIn(
                                initialAlpha = 0.3f,
                                animationSpec = tween(
                                    durationMillis = 600,
                                    delayMillis = idx * 100
                                )
                            ),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300))
                        ) {
                            MyRecipeCard(
                                recipe = recipe,
                                onNavigateToDetails = onNavigateToDetails
                            )
                        }
                    }
                }

                else -> {
                    item {
                        Text(
                            text = stringResource(R.string.profile_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.alpha(0.5f),
                        )
                    }
                }
            }
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
            contentDescription = stringResource(R.string.profile_picture_desc),
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                text = "${if (isNameEmpty) stringResource(R.string.profile_anonymous) else user.displayName}",
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
                contentDescription = stringResource(R.string.profile_sign_out_desc),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}