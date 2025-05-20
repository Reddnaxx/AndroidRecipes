package com.example.recipe_data.dataSources

import com.example.recipe_domain.dto.RecipeDto
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
                println("Error getting documents: $exception")
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
                println("Error getting documents: $exception")
            }
            .await()

        return result
    }

    suspend fun addRecipe(recipe: RecipeDto) {
        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                documentReference.update("id", documentReference.id)
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
            .await()
    }
}