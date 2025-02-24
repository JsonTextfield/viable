package com.jsontextfield.viable.network

import com.jsontextfield.viable.data.model.LatLon
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.toMixedCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class TrainService(private val client: HttpClient) {
    suspend fun getTrains(): List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        try {
            val response = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                val data = Json.parseToJsonElement(response.body<String>()).jsonObject.entries
                return data.map { entry ->
                    val jsonObject = entry.value.jsonObject
                    val latLon = if (jsonObject["lat"] != null && jsonObject["lng"] != null) {
                        LatLon(
                            jsonObject["lat"]?.jsonPrimitive?.doubleOrNull ?: 0.0,
                            jsonObject["lng"]?.jsonPrimitive?.doubleOrNull ?: 0.0,
                        )
                    }
                    else {
                        null
                    }
                    val stopsJson = jsonObject["times"]?.jsonArray
                    val stops = ArrayList<Stop>()
                    for (i in 0 until (stopsJson?.size ?: 0)) {
                        stopsJson?.get(i)?.jsonObject?.let(Stop::fromJson)?.let(stops::add)
                    }
                    val headsign =
                        "${jsonObject["from"]?.jsonPrimitive?.content} -> ${jsonObject["to"]?.jsonPrimitive?.content}".toMixedCase()
                    Train(
                        number = entry.key,
                        headsign = headsign,
                        departed = jsonObject["departed"]?.jsonPrimitive?.booleanOrNull
                            ?: false,
                        arrived = jsonObject["arrived"]?.jsonPrimitive?.booleanOrNull ?: false,
                        location = latLon,
                        stops = stops,
                    )
                }.sortedBy { train ->
                    train.number.split(" ").first().toInt()
                }.toList()
            }
        } catch (exception: IOException) {
            //Log.e("Downloader", exception.stackTraceToString())
        }
        return emptyList()
    }
}