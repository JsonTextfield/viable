package com.jsontextfield.viable.data.repositories

import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.Downloader
import kotlinx.coroutines.flow.Flow

class ViableRepository(
    private val db: ViaRailDatabase
) : Repository {
    override suspend fun getData(): List<Train> {
        return Downloader.downloadTrains()
    }

    override suspend fun getStation(id: String): Flow<Station> {
        return db.stationDao.getStation(id)
    }

    override suspend fun getLine(id: String): Flow<List<Shape>> {
        return db.shapeDao.getPoints(id)
    }

}
