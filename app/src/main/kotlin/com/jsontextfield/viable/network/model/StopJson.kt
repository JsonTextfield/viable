package com.jsontextfield.viable.network.model

import com.jsontextfield.viable.data.model.Stop
import kotlinx.serialization.Serializable

@Serializable
data class StopJson(
    val code: String = "",
    val station: String = "",
    val eta: String = "",
) {
    fun toStop(): Stop {
        return Stop(
            id = code,
            name = station,
            eta = eta,
        )
    }
}