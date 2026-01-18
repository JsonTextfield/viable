@file:OptIn(ExperimentalSerializationApi::class)

package com.jsontextfield.viable.data.model

import com.jsontextfield.viable.toMixedCase
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Train(
    val number: String = "",
    private val from: String = "",
    private val to: String = "",
    val departed: Boolean = false,
    val arrived: Boolean = false,
    val lat: Double? = null,
    val lng: Double? = null,
    @JsonNames("times") val stops: List<Stop> = emptyList(),
) {
    val routeNumber: String = number.split(' ').firstOrNull().orEmpty()
    val name: String = "$number $from -> $to".toMixedCase()
    val nextStop: Stop? = stops.firstOrNull { it.eta != "ARR" }
    val hasLocation: Boolean = lat != null && lng != null
    val isEnabled: Boolean = hasLocation && nextStop != null
}
