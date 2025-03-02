package com.jsontextfield.viable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.repositories.ITrainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViableViewModel(private val repo: ITrainRepository) : ViewModel() {

    private var _viableState: MutableStateFlow<ViableState> = MutableStateFlow(ViableState())
    val viableState: StateFlow<ViableState> get() = _viableState.asStateFlow()

    private var _timeRemaining: MutableStateFlow<Int> = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()

    private var timerJob: Job? = null

    init {
        start()
    }

    private fun start() {
        timerJob = timerJob ?: viewModelScope.launch {
            while (true) {
                if (timeRemaining.value <= 0) {
                    downloadData()
                    _timeRemaining.value = 30_000
                }
                else {
                    delay(1000)
                    _timeRemaining.value -= 1000
                }
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        timerJob = null
        super.onCleared()
    }

    fun onTrainSelected(train: Train?) {
        viewModelScope.launch {
            _viableState.update {
                // If the selected train changes, reset the selected station and get the new route line
                val selectedTrainChanged =
                    train?.toString() != viableState.value.selectedTrain?.toString()
                it.copy(
                    selectedTrain = train,
                    shouldMoveCamera = selectedTrainChanged,
                    selectedStation = if (selectedTrainChanged) null else it.selectedStation,
                    routeLine = if (selectedTrainChanged) {
                        repo.getLine(train?.number?.split(" ")?.first() ?: "")
                    }
                    else {
                        it.routeLine
                    },
                )
            }
        }
    }

    fun onStopSelected(stop: Stop?) {
        viewModelScope.launch {
            _viableState.update {
                it.copy(
                    selectedStation = stop?.let { repo.getStation(it.id) }
                )
            }
        }
    }

    private fun downloadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repo.getTrains()
            _viableState.update {
                it.copy(
                    trains = data
                )
            }
            val train: Train? =
                data.firstOrNull { it.number == viableState.value.selectedTrain?.number }
                    ?: data.firstOrNull { it.location != null }
                    ?: data.firstOrNull()
            onTrainSelected(train)
        }
    }
}