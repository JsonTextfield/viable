package com.jsontextfield.viable.data.database

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station

interface IViaRailDatabase {
    suspend fun getStation(code: String): Station?
    suspend fun getPoints(route: String): List<Shape>
}