package com.jsontextfield.viable.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class Trip(
    @ColumnInfo(name = "route_id") val routeId: String,
    @ColumnInfo(name = "service_id") val serviceId: String,
    @PrimaryKey @ColumnInfo(name = "trip_id")  val tripId: String,
    @ColumnInfo(name = "shape_id") val shapeId: String,
    @ColumnInfo(name = "trip_short_name") val tripShortName: String,
    @ColumnInfo(name = "trip_headsign") val tripHeadsign: String,
    @ColumnInfo(name = "direction_id") val directionId: Int,
)