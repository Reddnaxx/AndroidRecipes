package com.example.recipe_domain.dto

import com.google.firebase.firestore.PropertyName

data class RecipeUpdateDto(
    @PropertyName("name") val name: String? = null,
    @PropertyName("description") val description: String? = null,
    @PropertyName("ingredients") val ingredients: List<String>? = null,
    @PropertyName("instructions") val instructions: List<String>? = null,
    @PropertyName("imageUrl") val imageUrl: String? = null,
)
