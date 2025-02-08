package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train

interface ITrainRepository {
    suspend fun getTrains(): List<Train>

    suspend fun getStation(id: String): Station

    suspend fun getLine(id: String): List<Shape>
}
