@file:OptIn(ExperimentalSerializationApi::class)

package com.jsontextfield.viable.data.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
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
    private val lat: Double? = null,
    private val lng: Double? = null,
    @JsonNames("times") val stops: List<Stop> = emptyList(),
) {
    val name: String
        get() = "$number $from -> $to".toMixedCase()

    val nextStop: Stop?
        get() = stops.find { it.eta != "ARR" }

    val location: LatLon?
        get() = if (lat != null && lng != null) LatLon(lat, lng) else null
}

fun String.toMixedCase(): String {
    return toLowerCase(Locale.current)
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}

fun String.replaceHtmlEntities(): String {
    return this.replace("&mdash;", "—")
}
