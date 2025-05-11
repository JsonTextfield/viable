package com.jsontextfield.viable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LatLon(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
)