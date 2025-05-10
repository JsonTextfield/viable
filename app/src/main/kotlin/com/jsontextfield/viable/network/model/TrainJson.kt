package com.jsontextfield.viable.network.model

import com.jsontextfield.viable.data.model.LatLon
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.toMixedCase
import kotlinx.serialization.Serializable

@Serializable
data class TrainJson(
    val id: String = "",
    val lat: Double? = null,
    val lng: Double? = null,
    val from: String = "",
    val to: String = "",
    val arrived: Boolean = false,
    val departed: Boolean = false,
    val times: List<StopJson> = emptyList(),
) {
    fun toTrain(): Train {
        return Train(
            number = id,
            headsign = "$from -> $to".toMixedCase(),
            location = if (lat != null && lng != null) {
                LatLon(lat, lng)
            } else {
                null
            },
            arrived = arrived,
            departed = departed,
            stops = times.map { it.toStop() },
        )
    }
}