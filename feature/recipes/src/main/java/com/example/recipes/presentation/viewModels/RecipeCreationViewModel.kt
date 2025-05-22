package com.example.recipes.presentation.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s3.domain.repository.S3Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface RecipeCreationState {
    val name: String
    val imageUri: String
    val description: String
    val ingredients: List<String>
    val steps: MutableList<String>
    val isValid: Boolean
}

@HiltViewModel
class RecipeCreationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val s3Repository: S3Repository,
) : ViewModel() {

    private val mutableState = MutableRecipeCreationState()
    val state: RecipeCreationState = mutableState

    fun createRecipe() = viewModelScope.launch {
        if (!mutableState.isValid) return@launch

        val pickedUri = mutableState.imageUri.toUri()
        val fileToUpload = uriToFile(pickedUri)

        val remoteUrl = withContext(Dispatchers.IO) {
            s3Repository.uploadFile(
                filePath = fileToUpload.absolutePath,
                objectKey = "images/${System.currentTimeMillis()}.jpg"
            )
        }

        mutableState.imageUri = remoteUrl
    }

    fun setName(name: String) {
        mutableState.name = name
    }

    fun setImageUri(imageURI: String) {
        mutableState.imageUri = imageURI
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
        mutableState.steps += ""
    }

    fun updateStep(idx: Int, step: String) {
        if (idx < 0 || idx >= mutableState.steps.size) {
            throw IndexOutOfBoundsException("Invalid index: $idx")
        }

        mutableState.steps[idx] = step
    }

    fun removeStep(idx: Int) {
        mutableState.steps.removeAt(idx)
    }

    fun clearState() {
        mutableState.name = ""
        mutableState.imageUri = ""
        mutableState.description = ""
        mutableState.ingredients = emptyList()
        mutableState.steps = mutableListOf()
    }

    fun storeBitmapToCache(bitmap: Bitmap): Uri {
        val cacheDir = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use {
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
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

    class MutableRecipeCreationState : RecipeCreationState {
        override var name: String by mutableStateOf("")
        override var imageUri: String by mutableStateOf("")
        override var description: String by mutableStateOf("")
        override var ingredients: List<String> by mutableStateOf(emptyList())
        override var steps: MutableList<String> = mutableStateListOf("")

        override val isValid: Boolean
            get() = name.isNotBlank() && imageUri.isNotBlank() && description.isNotBlank()
                    && ingredients.isNotEmpty() && steps.any { it.isNotBlank() }
    }
}