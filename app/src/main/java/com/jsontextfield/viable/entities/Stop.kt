package com.jsontextfield.viable.entities

import org.json.JSONObject

data class Stop(
    var name: String = "",
    var eta: String = "",
) {
    companion object {
        fun fromJson(jsonObject: JSONObject): Stop {
            return Stop(
                name = jsonObject.optString("station"),
                eta = jsonObject.optString("eta"),
            )
        }
    }
}