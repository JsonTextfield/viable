package com.jsontextfield.viable.data.model

import org.json.JSONObject
import java.time.format.DateTimeFormatter

data class Stop(
    val name: String = "",
    val eta: String = "",
    val id: String = "",
    val delay: Int = 0,
    val scheduled: String = "",
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Stop {
            val inFormatter = DateTimeFormatter.ISO_INSTANT
            val outFormatter = DateTimeFormatter.ofPattern("HH:mm")
            //val schedule = outFormatter.format(inFormatter.parse(jsonObject.optString("scheduled")))
            return Stop(
                name = jsonObject.optString("station"),
                eta = jsonObject.optString("eta"),
                id = jsonObject.optString("code"),
                delay = jsonObject.optInt("diffMin"),
                //scheduled = schedule,
            )
        }
    }
}