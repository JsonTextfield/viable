package com.jsontextfield.viable.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsontextfield.viable.Station

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1)
abstract class ViaRailDatabase : RoomDatabase() {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao
}