package com.jsontextfield.viable.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SelectText(
    text: String,
    defaultColor: Color,
    isSelected: Boolean = false,
) {
    Text(
        text,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        }
        else {
            defaultColor
        }
    )
}