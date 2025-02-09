package com.jsontextfield.viable.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.LatLon
import com.jsontextfield.viable.data.model.Train

@Composable
actual fun ViableMap(
    position: LatLon,
    modifier: Modifier,
    selectedTrain: Train?,
    selectedStation: Station?,
    routeLine: List<Shape>,
) {
}