package com.jsontextfield.viable.entities

import com.google.android.gms.maps.model.LatLng

data class LatLon(val lat: Double, val lon: Double) {
    fun toLatLng() = LatLng(lat, lon)

    override fun toString() = "[$lat, $lon]"
}