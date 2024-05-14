package com.jsontextfield.viable.entities

import com.google.android.gms.maps.model.LatLng

data class LatLon(val lat: Double, val lon: Double) {
    fun toLatLng() = LatLng(lat, lon)

    override fun toString() = "[$lat, $lon]"

    override fun hashCode(): Int {
        var result = lat.hashCode()
        result = 31 * result + lon.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LatLon

        if (lat != other.lat) return false
        if (lon != other.lon) return false

        return true
    }
}