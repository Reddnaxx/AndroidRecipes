package com.example.recipes.presentation.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_data.useCases.RecipeEditUseCase
import com.example.recipe_domain.dto.RecipeUpdateDto
import com.example.s3.domain.repository.S3Repository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface RecipeEditState {
    val currentId: String
    val name: String
    val image: String
    val description: String
    val ingredients: List<String>
    val instructions: MutableList<String>
    val isValid: Boolean
    val isSubmitting: Boolean
    val isDeleting: Boolean
    val isImageChanged: Boolean
    val isLoading: Boolean
    val isLoaded: Boolean
}

@HiltViewModel
class RecipeEditViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val s3Repository: S3Repository,
    private val useCase: RecipeEditUseCase
) : ViewModel() {

    private val user = Firebase.auth.currentUser

    private val mutableState = MutableRecipeEditState()
    val state: RecipeEditState = mutableState

    fun updateRecipe(onComplete: () -> Unit = {}) = viewModelScope.launch {
        if (mutableState.isValid.not() || user == null) return@launch

        mutableState.isSubmitting = true

        val image = if (mutableState.isImageChanged) {
            val pickedUri = mutableState.image.toUri()
            val fileToUpload = uriToFile(pickedUri)

            withContext(Dispatchers.IO) {
                try {
                    s3Repository.uploadFile(
                        filePath = fileToUpload.absolutePath,
                        objectKey = "images/${System.currentTimeMillis()}.jpg"
                    )
                } catch (e: Exception) {
                    Log.e("RecipeCreationViewModel", "Error uploading file", e)
                    return@withContext null
                }
            }
        } else {
            mutableState.image
        }

        if (image == null) {
            Log.e("RecipeCreationViewModel", "Failed to upload image")
            mutableState.isSubmitting = false
            return@launch
        }

        try {
            useCase.updateRecipe(
                mutableState.currentId,
                RecipeUpdateDto(
                    name = mutableState.name,
                    imageUrl = image,
                    description = mutableState.description,
                    ingredients = mutableState.ingredients,
                    instructions = mutableState.instructions.filter { it.isNotBlank() }
                )
            )
        } catch (e: Exception) {
            Log.e("RecipeCreationViewModel", "Error creating recipe", e)
            return@launch
        } finally {
            mutableState.isSubmitting = false
        }

        onComplete()
        clearState()
    }

    fun deleteRecipe(onComplete: () -> Unit = {}) = viewModelScope.launch {
        if (mutableState.currentId.isBlank()) return@launch

        mutableState.isDeleting = true

        try {
            useCase.deleteRecipe(mutableState.currentId)
        } catch (e: Exception) {
            throw e
        }

        val imageKey = mutableState.image.substringAfterLast("images/")

        withContext(Dispatchers.IO) {
            try {
                s3Repository.deleteFile(imageKey)
            } catch (e: Exception) {
                Log.e("RecipeEditViewModel", "Error deleting image", e)
            }
        }

        mutableState.isDeleting = false

        onComplete()
        clearState()
    }

    fun initialize(id: String) {
        mutableState.currentId = id

        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        mutableState.isLoaded = false
        mutableState.isLoading = true

        val recipe = useCase.getRecipeById(mutableState.currentId)

        if (recipe == null) {
            Log.e(
                "RecipeEditViewModel",
                "Failed to load recipe with id: ${mutableState.currentId}"
            )
            return@launch
        }

        mutableState.name = recipe.name
        mutableState.image = recipe.imageUrl
        mutableState.description = recipe.description
        mutableState.ingredients = recipe.ingredients
        mutableState.instructions = recipe.instructions.toMutableStateList()
        mutableState.isImageChanged = false

        mutableState.isLoaded = true
        mutableState.isLoading = false
    }

    fun setName(name: String) {
        mutableState.name = name
    }

    fun setImageUri(imageURI: String) {
        mutableState.image = imageURI
        mutableState.isImageChanged = true
    }

    fun setDescription(description: String) {
        mutableState.description = description
    }

    fun addIngredient(ingredient: String) {
        if (mutableState.ingredients.contains(ingredient)) return
        mutableState.ingredients += ingredient
    }

    fun removeIngredient(ingredient: String) {
        mutableState.ingredients =
            mutableState.ingredients.filter { it != ingredient }
    }

    fun addStep() {
        mutableState.instructions += ""
    }

    fun updateStep(idx: Int, step: String) {
        if (idx < 0 || idx >= mutableState.instructions.size) {
            throw IndexOutOfBoundsException("Invalid index: $idx")
        }

        mutableState.instructions[idx] = step
    }

    fun removeStep(idx: Int) {
        mutableState.instructions.removeAt(idx)
    }

    fun clearState() {
        mutableState.currentId = ""
        mutableState.name = ""
        mutableState.image = ""
        mutableState.description = ""
        mutableState.ingredients = emptyList()
        mutableState.instructions = mutableListOf()
        mutableState.isSubmitting = false
        mutableState.isImageChanged = false
        mutableState.isLoaded = false
        mutableState.isLoading = false
    }

    fun storeBitmapToCache(bitmap: Bitmap): Uri {
        val cacheDir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(cacheDir, "image_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use {
            bitmap.compress(
                Bitmap.CompressFormat.WEBP,
                100,
                it
            )
        }
        return Uri.fromFile(file)
    }

    private fun uriToFile(uri: Uri): File {
        val inStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Unable to open URI: $uri")

        val cacheDir =
            File(context.cacheDir, "upload_images").apply { mkdirs() }
        val outFile = File(cacheDir, "img_${System.currentTimeMillis()}.jpg")

        FileOutputStream(outFile).use { out ->
            inStream.use { input ->
                input.copyTo(out)
            }
        }

        return outFile
    }

    class MutableRecipeEditState : RecipeEditState {
        override var currentId: String by mutableStateOf("")

        override var name: String by mutableStateOf("")
        override var image: String by mutableStateOf("")
        override var description: String by mutableStateOf("")
        override var ingredients: List<String> by mutableStateOf(emptyList())
        override var instructions: MutableList<String> = mutableStateListOf("")

        override var isSubmitting: Boolean by mutableStateOf(false)
        override var isDeleting: Boolean by mutableStateOf(false)

        override var isImageChanged: Boolean by mutableStateOf(false)

        override var isLoading: Boolean by mutableStateOf(false)
        override var isLoaded: Boolean by mutableStateOf(false)

        override val isValid: Boolean
            get() = name.isNotBlank() && image.isNotBlank() && description.isNotBlank()
                    && ingredients.isNotEmpty() && instructions.any { it.isNotBlank() }
    }
}