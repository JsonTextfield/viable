package com.jsontextfield.viable.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stops")
data class Station(
    @PrimaryKey @ColumnInfo(name = "stop_id") val id: String,
    @ColumnInfo(name = "stop_code") val code: String,
    @ColumnInfo(name = "stop_name") val name: String,
    @ColumnInfo(name = "location_type") val locationType: Int,
    @ColumnInfo(name = "stop_lat") val lat: Double,
    @ColumnInfo(name = "stop_lon") val lon: Double,
    @ColumnInfo(name = "stop_timezone") val timezone: String,
    @ColumnInfo(name = "parent_station") val parentStation: String,
    @ColumnInfo(name = "wheelchair_boarding") val wheelchairBoarding: Int,
)