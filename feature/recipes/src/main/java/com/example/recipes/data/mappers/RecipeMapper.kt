package com.example.recipes.data.mappers

import com.example.recipes.data.dto.RecipeDto
import com.example.recipes.domain.models.Recipe

fun RecipeDto.toDomain() =
    Recipe(id, name, description, ingredients, instructions, imageUrl)

fun Recipe.toDto() =
    RecipeDto(id, name, description, ingredients, instructions, imageUrl)