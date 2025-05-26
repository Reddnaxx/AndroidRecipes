package com.example.local

import com.example.profile.domain.entities.RecipeEntity
import com.example.recipe_domain.models.Recipe

fun RecipeEntity.toDomainModel(): Recipe = Recipe(
    id = id,
    name = name,
    description = description,
    ingredients = ingredients,
    instructions = instructions,
    imageUrl = imageUrl,
    authorId = authorId,
    createdAt = createdAt
)

fun Recipe.toEntity(): RecipeEntity = RecipeEntity(
    id = id,
    name = name,
    description = description,
    ingredients = ingredients,
    instructions = instructions,
    imageUrl = imageUrl,
    authorId = authorId,
    createdAt = createdAt
)