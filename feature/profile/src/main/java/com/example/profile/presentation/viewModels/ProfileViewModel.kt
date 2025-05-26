package com.example.profile.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth_domain.IAuthRepository
import com.example.local.AppDatabase
import com.example.local.toDomainModel
import com.example.local.toEntity
import com.example.network.NetworkMonitor
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
    private val recipeProfileUseCase: RecipeProfileUseCase,
    private val networkMonitor: NetworkMonitor,
    private val db: AppDatabase
) : ViewModel() {

    val user = Firebase.auth.currentUser

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        networkMonitor.register()

        refresh()
    }

    fun refresh() = viewModelScope.launch {
        if (networkMonitor.isConnected.value == true) {
            getUserRecipes()
        } else {
            getLocalRecipes()
        }
    }

    private suspend fun getLocalRecipes() {
        try {
            _isLoading.value = true
            _recipes.value =
                db.recipeDao().getAllRecipes().map { it.toDomainModel() }
        } catch (e: Exception) {
            Log.d(
                "ProfileViewModel",
                e.message ?: "Unknown error while fetching local recipes"
            )
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun getUserRecipes() {
        user?.let {
            try {
                _isLoading.value = true
                _recipes.value = recipeProfileUseCase.getUserRecipes(it.uid)
                db.recipeDao().insertRecipes(_recipes.value.map { recipe ->
                    recipe.toEntity()
                })
            } catch (e: Exception) {
                Log.d(
                    "ProfileViewModel",
                    e.message ?: "Unknown error while fetching recipes"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        authRepo.signOut()
    }

    override fun onCleared() {
        super.onCleared()
        networkMonitor.unregister()
    }
}