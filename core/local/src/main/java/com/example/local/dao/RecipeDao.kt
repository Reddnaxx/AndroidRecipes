package com.example.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.profile.domain.entities.RecipeEntity

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes")
    suspend fun clearAll()
}