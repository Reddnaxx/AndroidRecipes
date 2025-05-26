package com.example.recipes.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.network.NetworkMonitor
import com.example.recipe_data.useCases.RecipeFavoritesUseCase
import com.example.recipe_data.useCases.RecipeListUseCase
import com.example.recipe_domain.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipeListUseCase: RecipeListUseCase,
    private val recipeFavoritesUseCase: RecipeFavoritesUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val isNetworkAvailable = networkMonitor.isConnected.asFlow()

    init {
        networkMonitor.register()

        viewModelScope.launch {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .collect {
                    updateRecipes()
                }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    private suspend fun updateRecipes() {
        try {
            _isLoading.value = true
            _recipes.value = recipeListUseCase.getRecipes(_query.value)
        } catch (e: Exception) {
            println("Error fetching recipes: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    fun addToFavorites(recipeId: String) = viewModelScope.launch {
        try {
            recipeFavoritesUseCase.addRecipeToFavorites(recipeId)
            updateRecipes()
        } catch (e: Exception) {
            Log.e(
                "RecipeListViewModel",
                "Error adding to favorites: ${e.message}"
            )
        }
    }

    fun removeFromFavorites(recipeId: String) = viewModelScope.launch {
        try {
            recipeFavoritesUseCase.removeRecipeFromFavorites(recipeId)
            updateRecipes()
        } catch (e: Exception) {
            Log.e(
                "RecipeListViewModel",
                "Error removing from favorites: ${e.message}"
            )
        }
    }

    fun refreshRecipes() {
        viewModelScope.launch {
            updateRecipes()
        }
    }

    override fun onCleared() {
        super.onCleared()

        networkMonitor.unregister()
    }
}