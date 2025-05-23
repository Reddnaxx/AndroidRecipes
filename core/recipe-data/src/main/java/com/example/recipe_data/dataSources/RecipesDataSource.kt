package com.example.recipe_data.dataSources

import android.util.Log
import com.example.recipe_domain.dto.RecipeDto
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesDataSource @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getRecipes(authorId: String? = null): List<RecipeDto> {
        val recipesList = mutableListOf<RecipeDto>()

        val collection = if (authorId != null) {
            db.collection("recipes").whereEqualTo("authorId", authorId)
        } else {
            db.collection("recipes")
        }

        collection
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val recipe = document.toObject(RecipeDto::class.java)
                    recipesList.add(recipe)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "RecipesDataSource",
                    "Error getting documents: $exception"
                )
            }
            .await()
        return recipesList
    }

    suspend fun getRecipeById(id: String): RecipeDto? {
        var result: RecipeDto? = null

        db.collection("recipes")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    result = document.toObject(RecipeDto::class.java)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(
                    "RecipesDataSource",
                    "Error getting documents: $exception"
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
                Log.d("RecipesDataSource", "Error adding document: $e")
            }
            .await()
    }

    suspend fun updateRecipe(recipeId: String, recipe: RecipeUpdateDto) {
        db.collection("recipes")
            .document(recipeId)
            .update(
                "name", recipe.name,
                "description", recipe.description,
                "ingredients", recipe.ingredients,
                "instructions", recipe.instructions,
                "imageUrl", recipe.imageUrl,
            )
            .addOnSuccessListener {
                Log.d(
                    "RecipesDataSource",
                    "Document $recipeId successfully updated!"
                )
            }
            .addOnFailureListener { e ->
                Log.d(
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
                Log.d(
                    "RecipesDataSource",
                    "Error deleting document ${recipeId}: $e"
                )
            }
            .await()
    }
}