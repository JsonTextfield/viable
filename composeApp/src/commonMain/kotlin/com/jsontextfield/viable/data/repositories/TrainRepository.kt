package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.IViaRailDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.TrainService

class TrainRepository(
    private val db: IViaRailDatabase,
    private val trainService: TrainService,
) : ITrainRepository {
    override suspend fun getTrains(): List<Train> {
        return trainService.getTrains()
    }

    override suspend fun getStation(id: String): Station? {
        return db.getStation(id)
    }

    override suspend fun getLine(id: String): List<Shape> {
        return db.getPoints(id)
    }

}
