package com.jsontextfield.viable.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.data.entities.Station
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Query("SELECT * FROM stops WHERE stop_code = :code")
    fun getStation(code: String): Flow<Station>
}
