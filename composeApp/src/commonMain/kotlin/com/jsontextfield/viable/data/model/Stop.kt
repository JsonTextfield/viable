@file:OptIn(ExperimentalSerializationApi::class)

package com.jsontextfield.viable.data.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class Stop(
    @JsonNames("code") val id: String = "",
    @JsonNames("station") val name: String = "",
    val eta: String = "",
)