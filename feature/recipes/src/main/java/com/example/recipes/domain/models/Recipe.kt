package com.example.recipes.domain.models

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String?
)