package com.example.recipes.data.dto

import com.google.firebase.firestore.PropertyName

data class RecipeDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("description") val description: String = "",
    @PropertyName("ingredients") val ingredients: List<String> = emptyList(),
    @PropertyName("instructions") val instructions: List<String> = emptyList(),
    @PropertyName("imageUrl") val imageUrl: String? = null
)
