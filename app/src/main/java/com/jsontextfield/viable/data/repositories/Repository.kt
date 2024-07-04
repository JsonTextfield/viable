package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Train
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getData(): List<Train>

    suspend fun getStation(id: String): Flow<Station>

    suspend fun getLine(id: String): Flow<List<Shape>>
}
