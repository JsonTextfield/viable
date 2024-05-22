package com.jsontextfield.viable.entities

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import org.json.JSONObject

data class Train(
    val number: String,
    val headsign: String,
    val departed: Boolean,
    val arrived: Boolean,
    val location: LatLon?,
    val stops: List<Stop>,
) {
    override fun toString() = "$number $headsign"

    val nextStop: Stop?
        get() = stops.find { it.eta != "ARR" }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + headsign.hashCode()
        result = 31 * result + departed.hashCode()
        result = 31 * result + arrived.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Train

        if (number != other.number) return false
        if (headsign != other.headsign) return false
        if (departed != other.departed) return false
        if (arrived != other.arrived) return false
        if (location != other.location) return false

        return true
    }

    companion object {
        fun fromJson(jsonObject: JSONObject): Train {
            val latLon = if (!jsonObject.isNull("lat") && !jsonObject.isNull("lng")) {
                LatLon(
                    jsonObject.getDouble("lat"),
                    jsonObject.getDouble("lng"),
                )
            }
            else {
                null
            }
            val stopsJson = jsonObject.getJSONArray("times")
            val stops = ArrayList<Stop>()
            for (i in 0 until stopsJson.length()) {
                stops.add(Stop.fromJson(stopsJson.getJSONObject(i)))
            }
            return Train(
                number = jsonObject.optString("number", ""),
                headsign = "${jsonObject.optString("from")} -> ${jsonObject.optString("to")}".toMixedCase(),
                departed = jsonObject.optBoolean("departed"),
                arrived = jsonObject.optBoolean("arrived"),
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
