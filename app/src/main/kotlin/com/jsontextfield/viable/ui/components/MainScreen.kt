package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.ui.ViableState
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viableState: ViableState,
    timeRemaining: Int,
    onTrainSelected: (Train?) -> Unit,
    onStopSelected: (Stop?) -> Unit,
    isPortrait: Boolean = true,
) {
    val cameraPositionState = rememberCameraPositionState()
    val listState = rememberLazyListState()
    val selectedTrain = viableState.selectedTrain
    val shouldMoveCamera = viableState.shouldMoveCamera
    val selectedStation = viableState.selectedStation
    val trains = viableState.trains
    val routeLine = viableState.routeLine

    LaunchedEffect(selectedTrain) {
        selectedTrain?.let { train ->
            train.location?.let {
                if (shouldMoveCamera) {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
                }
            }
            // Initial stop selection
            if (selectedStation == null) {
                train.nextStop?.let(onStopSelected)
                listState.scrollToItem(max(0, train.stops.indexOfFirst { it == train.nextStop }))
            }
        }
    }
    if (isPortrait) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        TrainComboBox(
                            items = trains,
                            selectedItem = selectedTrain,
                            onItemSelected = onTrainSelected,
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                Box(
                    modifier = Modifier.weight(.5f),
                ) {
                    ViableMap(
                        cameraPositionState = cameraPositionState,
                        selectedTrain = selectedTrain,
                        selectedStation = selectedStation,
                        routeLine = routeLine,
                    )
                    CountdownTimer(
                        timeRemaining = timeRemaining,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                    )
                }
                StopsList(
                    stops = selectedTrain?.stops ?: emptyList(),
                    selectedStation = selectedStation,
                    modifier = Modifier.weight(.3f),
                    listState = listState,
                    onItemClick = onStopSelected
                )
            }
        }
    }
    else {
        Scaffold { innerPadding ->
            Row {
                Column(Modifier
                    .weight(1f)
                    .padding(top = innerPadding.calculateTopPadding())) {
                    TrainComboBox(
                        items = trains,
                        selectedItem = selectedTrain,
                        onItemSelected = onTrainSelected,
                    )
                    StopsList(
                        stops = selectedTrain?.stops ?: emptyList(),
                        selectedStation = selectedStation,
                        listState = listState,
                        onItemClick = onStopSelected
                    )
                }
                Box(modifier = Modifier.weight(2f)) {
                    ViableMap(
                        cameraPositionState = cameraPositionState,
                        selectedTrain = selectedTrain,
                        selectedStation = selectedStation,
                        routeLine = routeLine
                    )
                    CountdownTimer(
                        timeRemaining = timeRemaining,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = innerPadding.calculateTopPadding())
                            .padding(8.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        viableState = ViableState(),
        timeRemaining = 14000,
        onTrainSelected = { },
        onStopSelected = { },
        isPortrait = true
    )
}