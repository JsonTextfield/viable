package com.jsontextfield.viable.data.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

data class Train(
    val number: String = "",
    val headsign: String = "",
    val departed: Boolean = false,
    val arrived: Boolean = false,
    val location: LatLon? = null,
    val stops: List<Stop> = emptyList(),
) {
    val name: String
        get() = "$number $headsign".toMixedCase()

    val nextStop: Stop?
        get() = stops.find { it.eta != "ARR" }
}

fun String.toMixedCase(): String {
    return toLowerCase(Locale.current)
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}
