package com.jsontextfield.viable.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.database.entities.Trip

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1, exportSchema = false)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class ViaRailRoomDatabase : RoomDatabase(), IViaRailDatabase {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao

    override suspend fun getStation(code: String): Station? {
        return stationDao.getStation(code)
    }
    override suspend fun getPoints(route: String): List<Shape> {
        return shapeDao.getPoints(route) ?: emptyList()
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<ViaRailRoomDatabase> {
    override fun initialize(): ViaRailRoomDatabase
}