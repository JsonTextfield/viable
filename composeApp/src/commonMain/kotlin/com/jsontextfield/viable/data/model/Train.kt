package com.jsontextfield.viable.data.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class Train(
    val number: String = "",
    val headsign: String = "",
    val departed: Boolean = false,
    val arrived: Boolean = false,
    val location: LatLon? = null,
    val stops: List<Stop> = emptyList(),
) {
    override fun toString() = "$number $headsign"

    val nextStop: Stop?
        get() = stops.find { it.eta != "ARR" }

    companion object {
        fun fromJson(jsonObject: JsonObject): Train {
            val latLon = if (jsonObject["lat"] != null && jsonObject["lng"] != null) {
                LatLon(
                    jsonObject.get("lat")?.jsonPrimitive?.doubleOrNull ?: 0.0,
                    jsonObject.get("lng")?.jsonPrimitive?.doubleOrNull ?: 0.0,
                )
            } else {
                null
            }
            val stopsJson = jsonObject.get("times")?.jsonArray
            val stops = ArrayList<Stop>()
            for (i in 0 until (stopsJson?.size ?: 0)) {
                stopsJson?.get(i)?.jsonObject?.let(Stop::fromJson)?.let(stops::add)
            }
            val headsign =
                "${jsonObject["from"]?.jsonPrimitive?.content} -> ${jsonObject["to"]?.jsonPrimitive?.content}".toMixedCase()
            return Train(
                number = jsonObject["number"]?.jsonPrimitive?.contentOrNull ?: "",
                headsign = headsign,
                departed = jsonObject["departed"]?.jsonPrimitive?.booleanOrNull ?: false,
                arrived = jsonObject["arrived"]?.jsonPrimitive?.booleanOrNull ?: false,
                location = latLon,
                stops = stops,
            )
        }
    }
}

fun String.toMixedCase(): String {
    return toLowerCase(Locale.current)
        .split(" ")
        .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
}
