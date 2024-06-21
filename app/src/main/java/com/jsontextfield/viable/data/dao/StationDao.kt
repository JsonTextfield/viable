package com.jsontextfield.viable.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.data.entities.Station

@Dao
interface StationDao {
    @Query("SELECT * FROM stops WHERE stop_code = :code")
    suspend fun getStation(code: String): Station
}
