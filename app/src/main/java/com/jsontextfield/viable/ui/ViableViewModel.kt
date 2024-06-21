package com.jsontextfield.viable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.jsontextfield.viable.data.database.ViaRailDatabase
import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.network.Downloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViableViewModel(
    private val db: ViaRailDatabase,
) : ViewModel() {

    private var _selectedTrain: MutableStateFlow<Train?> = MutableStateFlow(null)
    val selectedTrain: StateFlow<Train?> get() = _selectedTrain.asStateFlow()

    private var _trains: MutableStateFlow<List<Train>> = MutableStateFlow(emptyList())
    val trains: StateFlow<List<Train>> get() = _trains.asStateFlow()

    fun onTrainSelected(train: Train) {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedTrain.emit(train)
        }
    }

    suspend fun getStation(stop: Stop): Station {
        return db.stationDao.getStation(stop.id)
    }

    suspend fun getLine(train: Train) : List<Shape> {
        return db.shapeDao.getPoints(train.number.split(" ").first())
    }

    fun downloadData() {
        viewModelScope.launch(Dispatchers.IO){
            _trains.emit(Downloader.downloadTrains())
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