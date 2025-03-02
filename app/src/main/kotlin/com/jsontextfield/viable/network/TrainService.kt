package com.jsontextfield.viable.network

import android.util.Log
import com.jsontextfield.viable.data.model.LatLon
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.toMixedCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import okio.IOException
import org.json.JSONObject

class TrainService(private val client: HttpClient) {
    suspend fun getTrains(): List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        try {
            val response = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                val trainsJson = JSONObject(response.body<String>())

                val data = trainsJson.keys().asSequence().map {
                    trainsJson.getJSONObject(it).put("number", it)

                    val jsonObject = trainsJson.getJSONObject(it)

                    val latLon = if (!jsonObject.isNull("lat") && !jsonObject.isNull("lng")) {
                        LatLon(
                            jsonObject.getDouble("lat"),
                            jsonObject.getDouble("lng"),
                        )
                    }
                    else {
                        null
                    }
                    val stopsJson = jsonObject.getJSONArray("times")
                    val stops = ArrayList<Stop>()
                    for (i in 0 until stopsJson.length()) {
                        stops.add(Stop.fromJson(stopsJson.getJSONObject(i)))
                    }
                    Train(
                        number = jsonObject.optString("number", ""),
                        headsign = "${jsonObject.optString("from")} -> ${jsonObject.optString("to")}".toMixedCase(),
                        departed = jsonObject.optBoolean("departed"),
                        arrived = jsonObject.optBoolean("arrived"),
                        location = latLon,
                        stops = stops,
                    )
                }.sortedBy { train ->
                    train.number.split(" ").first().toInt()
                }.toList()
                return data
            }
        } catch (exception: IOException) {
            Log.e("Downloader", exception.stackTraceToString())
        }
        return emptyList()
    }
}