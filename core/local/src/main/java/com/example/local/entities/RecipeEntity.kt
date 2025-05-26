package com.example.profile.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val imageUrl: String,
    val authorId: String,
    val createdAt: String
)