package com.example.profile.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth_domain.IAuthRepository
import com.example.recipe_data.useCases.RecipeProfileUseCase
import com.example.recipe_domain.models.Recipe
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepo: IAuthRepository,
    private val recipeProfileUseCase: RecipeProfileUseCase
) : ViewModel() {
    val user = Firebase.auth.currentUser

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    init {
        viewModelScope.launch {
            getUserRecipes()
        }
    }

    private suspend fun getUserRecipes() {
        user?.let {
            _recipes.value = recipeProfileUseCase.getUserRecipes(it.uid)
        }
    }

    fun signOut() = viewModelScope.launch {
        authRepo.signOut()
    }
}