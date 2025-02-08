package com.jsontextfield.viable.data.database.entities

import org.json.JSONObject
import java.util.Date

data class Schedule(
    var estimate: Date? = null,
    var schedule: Date? = null,
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Schedule {
            return Schedule(
                //estimate = jsonObject.optString("estimated"),
                //schedule = jsonObject.optString("scheduled"),
            )
        }
    }
}