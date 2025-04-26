package com.jsontextfield.viable.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.database.entities.Trip

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1)
abstract class ViaRailRoomDatabase : RoomDatabase(), IViaRailDatabase {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao

    override suspend fun getStation(code: String): Station {
        return stationDao.getStation(code)
    }
    override suspend fun getPoints(route: String): List<Shape> {
        return shapeDao.getPoints(route)
    }
}