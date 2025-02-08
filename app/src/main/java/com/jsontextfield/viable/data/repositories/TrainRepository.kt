package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.TrainService

class TrainRepository(
    private val db: ViaRailDatabase,
    private val trainService: TrainService,
) : ITrainRepository {
    override suspend fun getTrains(): List<Train> {
        return trainService.getTrains()
    }

    override suspend fun getStation(id: String): Station {
        return db.stationDao.getStation(id)
    }

    override suspend fun getLine(id: String): List<Shape> {
        return db.shapeDao.getPoints(id)
    }

}
