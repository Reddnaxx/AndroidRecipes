package com.example.ui.controls.input.presentation.componetns

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.example.theme.Spacing
import com.example.ui.R

@Composable
fun ImageSelector(
    imageUri: String,
    onTakePhoto: (Bitmap) -> Unit,
    onPickImage: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val pickImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onPickImage(it.toString()) }
    }

    val takePhotoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp: Bitmap? ->
        bmp?.let {
            onTakePhoto(it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        it.entries.forEach { entry ->
            if (entry.value) {
                when (entry.key) {
                    Manifest.permission.CAMERA -> takePhotoLauncher.launch(null)
                    Manifest.permission.READ_MEDIA_IMAGES -> pickImageLauncher.launch(
                        "image/*"
                    )
                }
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .heightIn(min = 160.dp, max = 200.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Аватар",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_image),
                    contentDescription = "Добавить фото",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                )
                Text(
                    text = "Выберите изображение",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    if (showDialog) {
        SelectImageOriginDialog(
            onDismiss = { showDialog = false },
            onPickImage = {
                val readPermission =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
                    else Manifest.permission.READ_EXTERNAL_STORAGE;

                permissionLauncher.launch(
                    arrayOf(
                        readPermission,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                showDialog = false
            },
            onTakePhoto = {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                showDialog = false
            }
        )
    }
}

@Composable
private fun SelectImageOriginDialog(
    onDismiss: () -> Unit,
    onPickImage: () -> Unit,
    onTakePhoto: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите источник изображения") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        text = {
            Column {
                Text(
                    "Галерея",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPickImage() }
                        .padding(16.dp)
                )
                Text(
                    "Камера",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTakePhoto() }
                        .padding(16.dp)
                )
            }
        }
    )
}