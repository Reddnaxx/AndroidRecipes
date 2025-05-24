package com.example.recipe_data.dataSources

import android.util.Log
import com.example.recipe_data.mappers.toDomain
import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.recipe_domain.models.Recipe
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesDataSource @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getRecipes(
        uid: String,
        authorId: String? = null
    ): List<Recipe> {
        val recipesList = mutableListOf<RecipeDto>()

        val collection = if (authorId != null) {
            db.collection("recipes").whereEqualTo("authorId", authorId)
        } else {
            db.collection("recipes")
        }

        val favoriteIds = getUserFavoriteIds(uid)

        collection
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val recipe = document.toObject(RecipeDto::class.java)
                    recipesList.add(recipe)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error getting documents: $e"
                )
            }
            .await()

        return recipesList.map {
            it.toDomain().copy(
                isFavorite = favoriteIds.contains(it.id)
            )
        }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        var result: Recipe? = null

        db.collection("recipes")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    result = document.toObject(RecipeDto::class.java).toDomain()
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error getting documents: $e"
                )
            }
            .await()

        return result
    }

    suspend fun createRecipe(recipe: RecipeDto) {
        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                documentReference.update("id", documentReference.id)
                Log.d(
                    "RecipesDataSource",
                    "Document added with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.e("RecipesDataSource", "Error adding document: $e")
            }
            .await()
    }

    suspend fun updateRecipe(recipeId: String, dto: RecipeUpdateDto) {
        db.collection("recipes")
            .document(recipeId)
            .update(
                "name", dto.name,
                "description", dto.description,
                "ingredients", dto.ingredients,
                "instructions", dto.instructions,
                "imageUrl", dto.imageUrl,
            )
            .addOnSuccessListener {
                Log.d(
                    "RecipesDataSource",
                    "Document $recipeId successfully updated!"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error updating document ${recipeId}: $e"
                )
            }
            .await()
    }

    suspend fun deleteRecipe(recipeId: String) {
        db.collection("recipes")
            .document(recipeId)
            .delete()
            .addOnSuccessListener {
                Log.d(
                    "RecipesDataSource",
                    "Document $recipeId successfully deleted!"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error deleting document ${recipeId}: $e"
                )
            }
            .await()
    }

    suspend fun getUserFavorites(uid: String): List<Recipe> {
        val userFavoriteIds = getUserFavoriteIds(uid)

        val userFavorites = mutableListOf<RecipeDto>()

        if (userFavoriteIds.isEmpty()) {
            return emptyList()
        }

        db.collection("recipes").whereIn("id", userFavoriteIds)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val recipe = document.toObject(RecipeDto::class.java)
                    userFavorites.add(recipe)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error getting user favorites: $e"
                )
            }
            .await()

        return userFavorites.map {
            it.toDomain().copy(
                isFavorite = true
            )
        }
    }

    private suspend fun getUserFavoriteIds(uid: String): List<String> {
        val userFavoritesId = mutableListOf<String>()

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val recipe = document.get("recipeId").toString()
                    userFavoritesId.add(recipe)
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error getting user favorites: $e"
                )
            }
            .await()

        return userFavoritesId
    }

    suspend fun addRecipeToFavorites(uid: String, recipeId: String) {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .add(
                mapOf(
                    "recipeId" to recipeId,
                )
            )
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "RecipesDataSource",
                    "Recipe added to favorites with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error adding recipe to favorites: $e"
                )
            }
            .await()
    }

    suspend fun removeRecipeFromFavorites(uid: String, recipeId: String) {
        db.collection("users")
            .document(uid)
            .collection("favorites")
            .whereEqualTo("recipeId", recipeId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    "RecipesDataSource",
                    "Error removing recipe from favorites: $e"
                )
            }
            .await()
    }
}