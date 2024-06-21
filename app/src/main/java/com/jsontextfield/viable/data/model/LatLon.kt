package com.jsontextfield.viable.data.model

data class LatLon(val lat: Double, val lon: Double) {
    override fun toString() = "[$lat, $lon]"
}