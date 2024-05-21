package com.jsontextfield.viable.entities

import org.json.JSONObject
import java.time.format.DateTimeFormatter

data class Stop(
    val name: String = "",
    val eta: String = "",
    val id: String = "",
    val delay: Int = 0,
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Stop {
            val dateFormat = DateTimeFormatter.ISO_INSTANT
            val scheduled = dateFormat.parse(jsonObject.optString("scheduled"))
            return Stop(
                name = jsonObject.optString("station"),
                eta = jsonObject.optString("eta"),
                id = jsonObject.optString("code"),
                delay = jsonObject.optInt("diffMin"),
            )
        }
    }
}