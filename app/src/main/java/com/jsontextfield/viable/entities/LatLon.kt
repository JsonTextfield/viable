package com.jsontextfield.viable.entities

data class LatLon(val lat: Double, val lon: Double) {
    override fun toString() = "[$lat, $lon]"
}