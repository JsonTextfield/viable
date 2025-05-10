package com.jsontextfield.viable.network

import android.util.Log
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.model.TrainJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import okio.IOException

class TrainService(private val client: HttpClient) {
    suspend fun getTrains(): List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        try {
            val response = client.get(url)
            return if (response.status == HttpStatusCode.OK) {
                response.body<Map<String, TrainJson>>().map {
                    it.value.copy(id = it.key).toTrain()
                }.sortedBy {
                    it.number.split(' ').first().toInt()
                }
            } else {
                emptyList()
            }
        } catch (exception: IOException) {
            Log.e("Downloader", exception.stackTraceToString())
        }
        return emptyList()
    }
}