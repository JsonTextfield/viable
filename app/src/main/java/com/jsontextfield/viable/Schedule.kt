package com.jsontextfield.viable

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import org.json.JSONObject
import java.util.Date

data class Schedule(
    var estimate: Date? = null,
    var schedule: Date? = null,
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Schedule {
            val sdf = SimpleDateFormat()
            return Schedule(
                estimate = sdf.parse(jsonObject.optString("estimated")),
                schedule = sdf.parse(jsonObject.optString("scheduled")),
            )
        }
    }
}