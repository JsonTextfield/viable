package com.jsontextfield.viable.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.database.entities.Trip

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1)
abstract class ViaRailDatabase : RoomDatabase() {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao
}