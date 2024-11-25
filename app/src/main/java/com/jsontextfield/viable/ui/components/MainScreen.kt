package com.jsontextfield.viable.ui.components

import android.content.res.Configuration
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.ui.ViableViewModel
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viableViewModel: ViableViewModel) {
    val configuration = LocalConfiguration.current
    val cameraPositionState = rememberCameraPositionState()
    val listState = rememberLazyListState()
    val viableState by viableViewModel.viableState.collectAsStateWithLifecycle()
    val selectedTrain = viableState.selectedTrain
    val shouldMoveCamera = viableState.shouldMoveCamera
    val selectedStation = viableState.selectedStation
    val trains = viableState.trains
    val routeLine = viableState.routeLine

    LaunchedEffect(Unit) {
        viableViewModel.downloadData()
    }
    LaunchedEffect(selectedTrain) {
        selectedTrain?.let { train ->
            train.location?.let {
                if (shouldMoveCamera) {
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(it.lat, it.lon)))
                }
            }
            // Initial stop selection
            if (selectedStation == null) {
                train.nextStop?.let(viableViewModel::onStopSelected)
                listState.scrollToItem(max(0, train.stops.indexOfFirst { it == train.nextStop }))
            }
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
                            onItemSelected = viableViewModel::onTrainSelected,
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(
                    modifier = Modifier.weight(.5f),
                ) {
                    ViableMap(
                        cameraPositionState = cameraPositionState,
                        selectedTrain = selectedTrain,
                        selectedStation = selectedStation,
                        routeLine = routeLine,
                    )
                    RepeatingTimer(
                        30_000L,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        onTimerExpired = viableViewModel::downloadData,
                    )
                }
                StopsList(
                    stops = selectedTrain?.stops ?: emptyList(),
                    selectedStation = selectedStation,
                    modifier = Modifier.weight(.3f),
                    listState = listState,
                    onItemClick = viableViewModel::onStopSelected
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
                        onItemSelected = viableViewModel::onTrainSelected,
                    )
                    StopsList(
                        stops = selectedTrain?.stops ?: emptyList(),
                        selectedStation = selectedStation,
                        listState = listState,
                        onItemClick = viableViewModel::onStopSelected
                    )
                }
                Box(modifier = Modifier.weight(2f)) {
                    ViableMap(
                        cameraPositionState = cameraPositionState,
                        selectedTrain = selectedTrain,
                        selectedStation = selectedStation,
                        routeLine = routeLine
                    )
                    RepeatingTimer(
                        30_000L,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        onTimerExpired = viableViewModel::downloadData,
                    )
                }
            }
        }
    }
}