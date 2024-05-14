package com.jsontextfield.viable.entities

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.text.intl.Locale
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