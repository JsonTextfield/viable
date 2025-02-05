package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.Downloader

class TrainRepository(
    private val db: ViaRailDatabase,
    private val downloader: Downloader,
) : ITrainRepository {
    override suspend fun getData(): List<Train> {
        return downloader.downloadTrains()
    }

    override suspend fun getStation(id: String): Station {
        return db.stationDao.getStation(id)
    }

    override suspend fun getLine(id: String): List<Shape> {
        return db.shapeDao.getPoints(id)
    }

}
