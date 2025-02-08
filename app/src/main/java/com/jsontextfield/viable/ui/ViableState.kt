package com.jsontextfield.viable.ui

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train

data class ViableState(
    val trains: List<Train> = emptyList(),
    val selectedTrain: Train? = null,
    val selectedStation: Station? = null,
    val routeLine: List<Shape> = emptyList(),
    val shouldMoveCamera: Boolean = false,
)
