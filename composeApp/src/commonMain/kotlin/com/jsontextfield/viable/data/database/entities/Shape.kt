package com.jsontextfield.viable.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "shapes", primaryKeys = ["shape_id", "shape_pt_sequence"])
data class Shape(
    @SerialName("shape_id") @ColumnInfo(name = "shape_id") val id: String,
    @SerialName("shape_pt_lat") @ColumnInfo(name = "shape_pt_lat") val lat: Double,
    @SerialName("shape_pt_lon") @ColumnInfo(name = "shape_pt_lon") val lon: Double,
    @SerialName("shape_pt_sequence") @ColumnInfo(name = "shape_pt_sequence") val sequence: Int,
    @SerialName("shape_dist_traveled") @ColumnInfo(name = "shape_dist_traveled") val distance: Double? = null,
)