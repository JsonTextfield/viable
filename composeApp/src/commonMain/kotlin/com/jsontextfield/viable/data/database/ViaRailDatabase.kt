package com.jsontextfield.viable.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.database.entities.Trip
import androidx.room.RoomDatabaseConstructor

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1)
@ConstructedBy(ViaRailDatabaseConstructor::class)
abstract class ViaRailDatabase : RoomDatabase() {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ViaRailDatabaseConstructor : RoomDatabaseConstructor<ViaRailDatabase> {
    override fun initialize(): ViaRailDatabase
}