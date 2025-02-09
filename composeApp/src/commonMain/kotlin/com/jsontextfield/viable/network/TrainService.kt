package com.jsontextfield.viable.network

import com.jsontextfield.viable.data.model.Train
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class TrainService(private val client: HttpClient) {
    suspend fun getTrains(): List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        try {
            val response = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                val jsonObject =
                    Json.parseToJsonElement(response.body<String>()).jsonObject
                val data = jsonObject.keys.map {
                    Train.fromJson(it, jsonObject[it]!!.jsonObject)
                }.sortedBy { train ->
                    train.number.split(" ").first().toInt()
                }.toList()

                return data
            }
        } catch (exception: IOException) {
            //Log.e("Downloader", exception.stackTraceToString())
        }
        return emptyList()
    }
}