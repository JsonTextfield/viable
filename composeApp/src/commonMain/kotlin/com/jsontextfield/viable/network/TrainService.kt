package com.jsontextfield.viable.network

import com.jsontextfield.viable.data.model.Train
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException

class TrainService(private val client: HttpClient) {
    suspend fun getTrains(): List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        try {
            val response = client.get(url)
            return if (response.status == HttpStatusCode.OK) {
                response.body<Map<String, Train>>().map {
                    it.value.copy(number = it.key)
                }.sortedBy {
                    it.routeNumber.toIntOrNull() ?: 0
                }
            } else {
                emptyList()
            }
        } catch (exception: IOException) {
        }
        return emptyList()
    }
}
