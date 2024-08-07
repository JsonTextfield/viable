package com.jsontextfield.viable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.jsontextfield.viable.ViableApplication
import com.jsontextfield.viable.data.entities.Shape
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.repositories.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ViableViewModel(private val repo: Repository) : ViewModel() {

    private var _selectedTrain: MutableStateFlow<Train?> = MutableStateFlow(null)
    val selectedTrain: StateFlow<Train?> get() = _selectedTrain.asStateFlow()

    private var _selectedStation: MutableStateFlow<Station?> = MutableStateFlow(null)
    val selectedStation: StateFlow<Station?> get() = _selectedStation.asStateFlow()

    private var _trains: MutableStateFlow<List<Train>> = MutableStateFlow(emptyList())
    val trains: StateFlow<List<Train>> get() = _trains.asStateFlow()

    private var _routeLine: MutableStateFlow<List<Shape>> = MutableStateFlow(emptyList())
    val routeLine: StateFlow<List<Shape>> get() = _routeLine.asStateFlow()

    fun onTrainSelected(train: Train?) {
        viewModelScope.launch {
            _selectedTrain.emit(train)
        }
    }

    fun onStopSelected(stop: Stop?) {
        viewModelScope.launch {
            if (stop == null) {
                _selectedStation.emit(null)
            }
            else {
                repo.getStation(stop.id).collectLatest {
                    _selectedStation.emit(it)
                }
            }
        }
    }

    fun getLine() {
        viewModelScope.launch {
            repo.getLine(_selectedTrain.value?.number?.split(" ")?.first() ?: "").collectLatest {
                _routeLine.emit(it)
            }
        }
    }

    fun downloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.getData()
            _trains.emit(data)
            val train: Train? = data.find { it.number == selectedTrain.value?.number }
                ?: data.find { it.location != null } ?: data.firstOrNull()
            onTrainSelected(train)
        }
    }

    companion object {
        val ViableViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                ViableViewModel((application as ViableApplication).repository)
            }
        }
    }
}