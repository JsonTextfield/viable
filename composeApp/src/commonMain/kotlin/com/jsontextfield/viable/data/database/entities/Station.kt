package com.jsontextfield.viable.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "stops")
data class Station(
    @PrimaryKey
    @SerialName("stop_id")
    @ColumnInfo(name = "stop_id")
    val id: String,

    @SerialName("stop_code")
    @ColumnInfo(name = "stop_code")
    val code: String,

    @SerialName("stop_name")
    @ColumnInfo(name = "stop_name")
    val name: String,

    @SerialName("location_type")
    @ColumnInfo(name = "location_type")
    val locationType: Int,

    @SerialName("stop_lat")
    @ColumnInfo(name = "stop_lat")
    val lat: Double,

    @SerialName("stop_lon")
    @ColumnInfo(name = "stop_lon")
    val lon: Double,

    @SerialName("stop_timezone")
    @ColumnInfo(name = "stop_timezone")
    val timezone: String,

    @SerialName("parent_station")
    @ColumnInfo(name = "parent_station")
    val parentStation: String,

    @SerialName("wheelchair_boarding")
    @ColumnInfo(name = "wheelchair_boarding")
    val wheelchairBoarding: Int? = null,
)