package com.jsontextfield.viable.ui

import com.jsontextfield.viable.entities.Train

data class ViableState(
    val selectedTrain: Train? = null,
    val trains: List<Train> = emptyList(),
)