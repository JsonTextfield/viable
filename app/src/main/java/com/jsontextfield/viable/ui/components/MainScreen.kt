package com.jsontextfield.viable.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.ui.ViableViewModel
import kotlinx.coroutines.delay
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viableViewModel: ViableViewModel) {
    val configuration = LocalConfiguration.current
    val trains by viableViewModel.trains.collectAsStateWithLifecycle()
    val selectedTrain by viableViewModel.selectedTrain.collectAsStateWithLifecycle()
    val selectedStation by viableViewModel.selectedStation.collectAsStateWithLifecycle()
    val routeLine by viableViewModel.routeLine.collectAsStateWithLifecycle()
    val cameraPositionState = rememberCameraPositionState()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            viableViewModel.downloadData()
            delay(30_000)
        }
    }
    LaunchedEffect(selectedTrain) {
        selectedTrain?.let { train ->
            train.location?.let {
                cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
            }
            viableViewModel.getLine()
            train.nextStop?.let { viableViewModel.onStopSelected(it) }
            listState.scrollToItem(max(0, train.stops.indexOfFirst { it == train.nextStop }))
        }
    }
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        TrainComboBox(
                            items = trains,
                            selectedItem = selectedTrain,
                            onItemSelected = { viableViewModel.onTrainSelected(it) },
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                ViableMap(
                    modifier = Modifier.weight(.5f),
                    cameraPositionState = cameraPositionState,
                    selectedTrain = selectedTrain,
                    selectedStation = selectedStation,
                    routeLine = routeLine,
                )
                StopsList(
                    stops = selectedTrain?.stops ?: emptyList(),
                    modifier = Modifier.weight(.3f),
                    listState = listState,
                    onItemClick = { viableViewModel.onStopSelected(it) }
                )
            }
        }
    }
    else {
        Scaffold { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {
                Column(Modifier.weight(1f)) {
                    TrainComboBox(
                        items = trains,
                        selectedItem = selectedTrain,
                        onItemSelected = { viableViewModel.onTrainSelected(it) },
                    )
                    StopsList(
                        stops = selectedTrain?.stops ?: emptyList(),
                        listState = listState,
                        onItemClick = { viableViewModel.onStopSelected(it) }
                    )
                }
                ViableMap(
                    modifier = Modifier.weight(2f),
                    cameraPositionState = cameraPositionState,
                    selectedTrain = selectedTrain,
                    selectedStation = selectedStation,
                    routeLine = routeLine
                )
            }
        }
    }
}