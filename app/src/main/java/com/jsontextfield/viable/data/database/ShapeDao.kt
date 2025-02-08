package com.jsontextfield.viable.data.database

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.data.database.entities.Shape

@Dao
fun interface ShapeDao {
    @Query("SELECT * FROM trips JOIN shapes ON trips.shape_id=shapes.shape_id WHERE trips.trip_short_name=:route ORDER BY shapes.shape_pt_sequence ASC")
    suspend fun getPoints(route: String): List<Shape>
}