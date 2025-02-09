package com.jsontextfield.viable.network

import android.util.Log
import com.jsontextfield.viable.data.model.Train
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
                val jsonObject = JSONObject(response.body<String>())

                val data = jsonObject.keys().asSequence().map {
                    jsonObject.getJSONObject(it).put("number", it)
                    Train.fromJson(jsonObject.getJSONObject(it))
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