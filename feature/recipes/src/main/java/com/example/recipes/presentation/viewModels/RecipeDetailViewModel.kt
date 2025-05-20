package com.example.recipes.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_data.useCases.RecipeDetailUseCase
import com.example.recipe_domain.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val useCase: RecipeDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<Recipe?>(null)
    val state = _state.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    private var _currentRecipeId: String? = null

    fun initialize(id: String) {
        _currentRecipeId = id

        refresh()
    }

    fun refresh() {
        if (_currentRecipeId.isNullOrEmpty()) {
            Log.d("RecipeDetailViewModel", "Current recipe id is not set")
            return
        }

        getRecipeById(_currentRecipeId as String)
    }

    private fun getRecipeById(id: String) = viewModelScope.launch {
        _isLoading.value = true
        
        try {
            _state.value = useCase.getRecipeById(id)
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
        } finally {
            _isLoading.value = false
        }
    }
}