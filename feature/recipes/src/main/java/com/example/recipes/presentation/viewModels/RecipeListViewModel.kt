package com.example.recipes.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_data.useCases.RecipeListUseCase
import com.example.recipe_domain.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val recipeListUseCase: RecipeListUseCase
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipes()
        }
    }

    private suspend fun getRecipes(): List<Recipe> {
        return recipeListUseCase.getRecipes()
    }

    private suspend fun updateRecipes() {
        try {
            _isLoading.value = true
            _recipes.value = getRecipes()
        } catch (e: Exception) {
            println("Error fetching recipes: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    fun refreshRecipes() {
        viewModelScope.launch {
            updateRecipes()
        }
    }
}