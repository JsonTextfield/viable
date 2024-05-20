package com.jsontextfield.viable.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "shapes", primaryKeys = ["shape_id", "shape_pt_sequence"])
data class Shape(
    @ColumnInfo(name = "shape_id") val id: String,
    @ColumnInfo(name = "shape_pt_lat") val lat: Double,
    @ColumnInfo(name = "shape_pt_lon") val lon: Double,
    @ColumnInfo(name = "shape_pt_sequence") val sequence: Int,
    @ColumnInfo(name = "shape_dist_traveled") val distance: Double,
)