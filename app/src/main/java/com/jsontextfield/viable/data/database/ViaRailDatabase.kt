package com.jsontextfield.viable.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jsontextfield.viable.data.dao.ShapeDao
import com.jsontextfield.viable.data.dao.StationDao
import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.entities.Trip

@Database(entities = [Shape::class, Trip::class, Station::class], version = 1)
abstract class ViaRailDatabase : RoomDatabase() {
    abstract val shapeDao: ShapeDao
    abstract val stationDao: StationDao

    companion object {
        @Volatile
        private var INSTANCE: ViaRailDatabase? = null
        fun getInstance(context: Context): ViaRailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ViaRailDatabase::class.java,
                    "viarail.db"
                ).createFromAsset("via.db").build()
                INSTANCE = instance
                instance
            }
        }
    }
}