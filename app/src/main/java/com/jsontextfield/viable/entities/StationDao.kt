package com.jsontextfield.viable.entities

import androidx.room.Dao
import androidx.room.Query

@Dao
interface StationDao {
    @Query("SELECT * FROM stops WHERE stop_code = :code")
    fun getStation(code: String): Station
}
