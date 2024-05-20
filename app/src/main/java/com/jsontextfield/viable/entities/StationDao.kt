package com.jsontextfield.viable.entities

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.Station

@Dao
interface StationDao {
    @Query("SELECT * FROM stops WHERE stop_code = :code")
    fun getStation(code: String): Station
}
