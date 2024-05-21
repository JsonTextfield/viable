package com.jsontextfield.viable.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jsontextfield.viable.Station
import com.jsontextfield.viable.ViableState
import com.jsontextfield.viable.entities.Shape
import com.jsontextfield.viable.entities.Train
import com.jsontextfield.viable.entities.ViaRailDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViableViewModel(private val db: ViaRailDatabase) : ViewModel() {
    private var _viableState = MutableStateFlow(ViableState())
    val viableState: StateFlow<ViableState>
        get() = _viableState

    fun onTrainSelected(train: Train) {
        _viableState.update {
            it.copy(selectedTrain = train)
        }
    }

    fun getNextStation(train: Train, callback: (station: Station?) -> Unit = {}) {
        train.nextStop?.let { nextStop ->
            CoroutineScope(Dispatchers.IO).launch {
                callback(db.stationDao.getStation(nextStop.id))
            }
        }
    }

    fun getLine(train: Train, callback: (shapes: List<Shape>) -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            callback(db.shapeDao.getPoints(train.number.split(" ").first()))
        }
    }

    fun downloadData(context: Context) {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val jsonObjectRequest = JsonObjectRequest(url, { response ->
            val data = response.keys().asSequence().map {
                response.getJSONObject(it).put("number", it)
                Train.fromJson(response.getJSONObject(it))
            }.sortedBy { train ->
                train.number.split(" ").first().toInt()
            }.toList()
            loadData(data)
        }, {
            loadData(ArrayList())
        })
        Volley.newRequestQueue(context).add(jsonObjectRequest)
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