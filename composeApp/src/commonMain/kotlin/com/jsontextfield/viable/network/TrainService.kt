package com.jsontextfield.viable.network

import com.jsontextfield.viable.data.model.Train
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlin.coroutines.cancellation.CancellationException
import co.touchlab.kermit.Logger as Kermit

class TrainService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Kermit
                        .withTag("HttpClient")
                        .d(message)
                }
            }
            level = LogLevel.INFO
        }
    }
    @Throws(IOException::class, CancellationException::class)
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
            throw exception
        }
    }
}
