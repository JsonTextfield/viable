package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Train

interface Repository {
    suspend fun getData(): List<Train>

    suspend fun getStation(id: String): Station

    suspend fun getLine(id: String): List<Shape>
}
