package com.jsontextfield.viable.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jsontextfield.viable.data.entities.Shape
import kotlinx.coroutines.flow.Flow

@Dao
interface ShapeDao {
    @Query("SELECT * FROM trips JOIN shapes ON trips.shape_id=shapes.shape_id WHERE trips.trip_short_name=:route ORDER BY shapes.shape_pt_sequence ASC")
    fun getPoints(route: String): Flow<List<Shape>>
}