package com.example.ui.controls.input.presentation.componetns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.theme.Spacing

@Composable
fun DynamicInputList(
    value: List<String>,
    label: String,
    inputLabel: String,
    onAdd: () -> Unit,
    onValueChange: (Int, String) -> Unit,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(value.size) {
        if (value.size > 1) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )

        value.forEachIndexed { idx, it ->
            DynamicInput(
                value = it,
                label = "$inputLabel ${idx + 1}",
                modifier = if (idx == value.lastIndex) {
                    Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                } else {
                    Modifier.fillMaxWidth()
                },
                onValueChange = { onValueChange(idx, it) },
                onRemove = { onRemove(idx) },
                onNext = {
                    onAdd()
                }
            )
        }

        Button(
            onClick = onAdd,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Добавить ${inputLabel.lowercase()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun DynamicInput(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        suffix = {
            Text(
                text = "Удалить",
                modifier = Modifier.clickable { onRemove() }
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun DynamicInputListPreview() {
    DynamicInputList(
        value = listOf("Порезать", "Отбить", "Пожарить"),
        label = "Шаги",
        inputLabel = "Шаг",
        onAdd = {},
        onRemove = {},
        onValueChange = { _, _ -> },
    )
}