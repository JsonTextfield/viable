package com.jsontextfield.viable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.jsontextfield.viable.entities.Shape
import com.jsontextfield.viable.entities.Station
import com.jsontextfield.viable.entities.Stop
import com.jsontextfield.viable.entities.Train
import com.jsontextfield.viable.entities.ViaRailDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ViableViewModel(private val db: ViaRailDatabase, private val client : OkHttpClient = OkHttpClient()) : ViewModel() {
    private var _viableState = MutableStateFlow(ViableState())
    val viableState: StateFlow<ViableState>
        get() = _viableState

    fun onTrainSelected(train: Train) {
        _viableState.update {
            it.copy(selectedTrain = train)
        }
    }

    fun getStation(stop: Stop, callback: (station: Station?) -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(db.stationDao.getStation(stop.id))
            }
        }
    }

    fun getLine(train: Train, callback: (shapes: List<Shape>) -> Unit = {}) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                callback(db.shapeDao.getPoints(train.number.split(" ").first()))
            }
        }
    }

    fun downloadData() {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                loadData(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body?.string() ?: "")

                val data = jsonObject.keys().asSequence().map {
                    jsonObject.getJSONObject(it).put("number", it)
                    Train.fromJson(jsonObject.getJSONObject(it))
                }.sortedBy { train ->
                    train.number.split(" ").first().toInt()
                }.toList()
                loadData(data)
            }
        })
    }

    private fun loadData(data: List<Train>) {
        _viableState.update {
            val selectedTrain = data.find { train ->
                train.number == it.selectedTrain?.number
            } ?: data.find { train ->
                train.location != null
            }

            it.copy(
                trains = data,
                selectedTrain = selectedTrain,
            )
        }
    }

    companion object {
        val ViableViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                ViableViewModel(
                    db = Room.databaseBuilder(
                        application.applicationContext,
                        ViaRailDatabase::class.java,
                        "viarail.db"
                    ).createFromAsset("via.db").build()
                )
            }
        }
    }
}