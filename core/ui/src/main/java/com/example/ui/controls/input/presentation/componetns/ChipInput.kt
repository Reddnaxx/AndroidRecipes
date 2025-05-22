package com.example.ui.controls.input.presentation.componetns

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.theme.Spacing

@Composable
fun ChipInput(
    value: List<String>,
    label: String,
    onAdd: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        placeholder = { Text("Добавить элемент") },
        modifier = modifier.fillMaxWidth(),
        suffix = {
            TextButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onAdd(text)
                        text = ""
                    }
                },
                enabled = text.isNotBlank()
            ) {
                Text("Добавить")
            }
        },
        supportingText = {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                modifier = Modifier.fillMaxWidth()
            ) {
                value.forEach { item ->
                    ItemChip(
                        text = item,
                        onDismiss = { onDelete(item) }
                    )
                }
            }
        }
    )
}

@Composable
private fun ItemChip(
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {

    InputChip(
        modifier = modifier,
        selected = false,
        onClick = onDismiss,
        colors = InputChipDefaults.inputChipColors(
            containerColor = MaterialTheme.colorScheme.primary,
            labelColor = MaterialTheme.colorScheme.onPrimary,
            trailingIconColor = MaterialTheme.colorScheme.onPrimary,
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Удалить элемент",
                modifier = Modifier.size(18.dp)
            )
        },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
    )
}

@Preview
@Composable
fun ChipInputPreview() {
    var items by remember {
        mutableStateOf(
            listOf(
                "Item 1",
                "Item 2",
                "Item 3"
            )
        )
    }

    ChipInput(
        value = items,
        label = "Добавить элемент",
        onAdd = { items = items + it },
        onDelete = { items = items - it }
    )
}