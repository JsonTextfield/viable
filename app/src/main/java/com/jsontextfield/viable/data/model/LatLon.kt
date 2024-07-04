package com.jsontextfield.viable.data.model

data class LatLon(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
) {
    override fun toString() = "[$lat, $lon]"
}