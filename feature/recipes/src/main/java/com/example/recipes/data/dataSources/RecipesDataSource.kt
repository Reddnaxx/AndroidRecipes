package com.example.recipes.data.dataSources

import com.example.recipes.data.dto.RecipeDto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecipesDataSource @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun getRecipes(): List<RecipeDto> {
        val recipesList = mutableListOf<RecipeDto>()
        db.collection("recipes")
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