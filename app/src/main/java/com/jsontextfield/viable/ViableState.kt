package com.jsontextfield.viable

import com.jsontextfield.viable.entities.Train

data class ViableState(
    val selectedTrain: Train? = null,
    val trains: List<Train> = emptyList(),
)