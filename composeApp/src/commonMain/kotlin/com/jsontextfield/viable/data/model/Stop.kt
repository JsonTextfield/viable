package com.jsontextfield.viable.data.model

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

data class Stop(
    val name: String = "",
    val eta: String = "",
    val id: String = "",
    val delay: Int = 0,
    val scheduled: String = "",
) {
    companion object {
        fun fromJson(jsonObject: JsonObject): Stop {
            //val inFormatter = DateTimeFormatter.ISO_INSTANT
            //val outFormatter = DateTimeFormatter.ofPattern("HH:mm")
            //val schedule = outFormatter.format(inFormatter.parse(jsonObject.optString("scheduled")))
            return Stop(
                name = jsonObject.get("station")?.jsonPrimitive?.contentOrNull ?: "",
                eta = jsonObject.get("eta")?.jsonPrimitive?.contentOrNull ?: "",
                id = jsonObject.get("code")?.jsonPrimitive?.contentOrNull ?: "",
                delay = jsonObject.get("diffMin")?.jsonPrimitive?.intOrNull ?: 0,
                //scheduled = schedule,
            )
        }
    }
}