package com.jsontextfield.viable.data.datasource

import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station

class ViaRailLocalDataSource(
    private val database: ViaRailDatabase
) : IViaRailDataSource {
    override suspend fun getRouteShape(route: String): List<Shape> {
        return database.shapeDao.getPoints(route)
    }

    override suspend fun getStation(code: String): Station {
        return database.stationDao.getStation(code)
    }
}