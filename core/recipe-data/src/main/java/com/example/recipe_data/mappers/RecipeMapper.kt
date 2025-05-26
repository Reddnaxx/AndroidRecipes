package com.example.recipe_data.mappers

import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.models.Recipe

fun RecipeDto.toDomain() =
    Recipe(
        id,
        name,
        description,
        ingredients,
        instructions,
        imageUrl,
        authorId,
        createdAt
    )

fun Recipe.toDto() =
    RecipeDto(
        id,
        name,
        description,
        ingredients,
        instructions,
        imageUrl,
        createdAt
    )