package com.jsontextfield.viable.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train

@Composable
expect fun ViableMap(
    modifier: Modifier = Modifier,
    shouldMoveCamera: Boolean = true,
    selectedTrain: Train? = null,
    selectedStation: Station? = null,
    routeLine: List<Shape> = emptyList(),
    isPortrait: Boolean = true,
)