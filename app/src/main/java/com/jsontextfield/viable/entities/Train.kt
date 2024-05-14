package com.jsontextfield.viable.entities

import android.text.Html
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import org.json.JSONObject

data class Train(
    var number: String,
    var departed: Boolean,
    var arrived: Boolean,
    var location: LatLon?,
    var stops: List<Stop>,
) {
    override fun toString(): String {
        return "$number $location"
    }

    private val nextStop: Stop?
        get() {
            return stops.find {
                it.eta != "ARR"
            }
        }

    val status: String
        get() {
            if (!departed) {
                return "Awaiting departure"
            }
            else if (arrived) {
                return "Arrived"
            }

            return "Next stop: ${nextStop?.name} in ${
                Html.fromHtml(
                    nextStop?.eta,
                    Html.FROM_HTML_MODE_LEGACY
                )
            }"
        }

    override fun hashCode(): Int {
        var result = number.hashCode()
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
            val stops = (0 until stopsJson.length()).map {
                Stop.fromJson(stopsJson.getJSONObject(it))
            }
            return Train(
                number =
                "${jsonObject.optString("number", "")} ${jsonObject.optString("train")} ${
                    jsonObject.optString("from").toMixedCase()
                } -> ${jsonObject.optString("to").toMixedCase()}",
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
