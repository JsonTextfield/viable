package com.jsontextfield.viable.network

import com.jsontextfield.viable.data.model.Train
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

object Downloader {
    private val client = OkHttpClient()

    fun downloadTrains() : List<Train> {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val jsonObject = JSONObject(response.body?.string() ?: "")

            val data = jsonObject.keys().asSequence().map {
                jsonObject.getJSONObject(it).put("number", it)
                Train.fromJson(jsonObject.getJSONObject(it))
            }.sortedBy { train ->
                train.number.split(" ").first().toInt()
            }.toList()
            return data
        }
        return emptyList()
    }
}