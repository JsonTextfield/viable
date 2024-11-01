package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.Downloader

class ViableRepository(
    private val db: ViaRailDatabase
) : Repository {
    override suspend fun getData(): List<Train> {
        return Downloader.downloadTrains()
    }

    override suspend fun getStation(id: String): Station {
        return db.stationDao.getStation(id)
    }

    override suspend fun getLine(id: String): List<Shape> {
        return db.shapeDao.getPoints(id)
    }

}
