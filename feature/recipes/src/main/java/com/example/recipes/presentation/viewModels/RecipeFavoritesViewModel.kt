package com.example.recipes.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_data.useCases.RecipeFavoritesUseCase
import com.example.recipe_domain.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeFavoritesViewModel @Inject constructor(
    private val useCase: RecipeFavoritesUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow(emptyList<Recipe>())
    val favorites = _favorites.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        refreshFavorites()
    }

    private suspend fun refreshFavorites() {
        try {
            _isLoading.value = true
            _favorites.value = useCase.getUserFavorites()

            Log.d(
                "FavoritesViewModel",
                "Favorites fetched successfully: ${_favorites.value.size} items"
            )
        } catch (e: Exception) {
            Log.e(
                "FavoritesViewModel",
                "Error fetching favorites: ${e.message}"
            )
        } finally {
            _isLoading.value = false
        }
    }

    fun addToFavorites(recipeId: String) = viewModelScope.launch {
        try {
            useCase.addRecipeToFavorites(recipeId)
        } catch (e: Exception) {
            Log.e(
                "FavoritesViewModel",
                "Error adding to favorites: ${e.message}"
            )
        }

        refreshFavorites()
    }

    fun removeFromFavorites(recipeId: String) = viewModelScope.launch {
        try {
            useCase.removeRecipeFromFavorites(recipeId)
        } catch (e: Exception) {
            Log.e(
                "FavoritesViewModel",
                "Error removing from favorites: ${e.message}"
            )
        }

        refreshFavorites()
    }
}