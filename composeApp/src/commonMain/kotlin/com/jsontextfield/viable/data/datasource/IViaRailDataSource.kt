package com.jsontextfield.viable.data.datasource

import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station

interface IViaRailDataSource {
    suspend fun getRouteShape(route: String) : List<Shape>
    suspend fun getStation(code: String) : Station
}