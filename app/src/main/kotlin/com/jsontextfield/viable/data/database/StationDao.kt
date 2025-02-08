package com.jsontextfield.viable.data.database

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.data.database.entities.Station

@Dao
fun interface StationDao {
    @Query("SELECT * FROM stops WHERE stop_code = :code")
    suspend fun getStation(code: String): Station
}
