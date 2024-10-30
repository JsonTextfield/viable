package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.R
import com.jsontextfield.viable.ui.ViableViewModel
import kotlinx.coroutines.delay
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viableViewModel: ViableViewModel) {
    val context = LocalContext.current
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
            GoogleMap(
                modifier = Modifier.weight(.5f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    minZoomPreference = 5f,
                    maxZoomPreference = 15f,
                    mapStyleOptions = if (isSystemInDarkTheme()) {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_mode)
                    }
                    else {
                        null
                    },
                ),
            ) {
                selectedTrain?.let { train ->
                    Polyline(
                        endCap = RoundCap(),
                        startCap = RoundCap(),
                        jointType = JointType.ROUND,
                        points = routeLine.map { LatLng(it.lat, it.lon) },
                        color = colorResource(id = R.color.primary_colour),
                    )
                    train.location?.let {
                        Marker(
                            state = MarkerState(position = LatLng(it.lat, it.lon)),
                            title = selectedTrain.toString(),
                            icon = BitmapDescriptorFactory.fromAsset("train.png"),
                            snippet = if (!train.departed) {
                                stringResource(id = R.string.departed)
                            }
                            else if (train.arrived) {
                                stringResource(id = R.string.arrived)
                            }
                            else {
                                stringResource(
                                    id = R.string.next_stop, train.nextStop?.name ?: "",
                                    Html.fromHtml(
                                        train.nextStop?.eta,
                                        Html.FROM_HTML_MODE_LEGACY
                                    ),
                                )
                            },
                        )
                    }
                }
                selectedStation?.let {
                    Marker(
                        icon = BitmapDescriptorFactory.fromAsset("station.png"),
                        state = MarkerState(position = LatLng(it.lat, it.lon)),
                        title = it.name,
                    )
                }
            }
            StopsList(
                stops = selectedTrain?.stops ?: emptyList(),
                modifier = Modifier.weight(.3f),
                listState = listState,
                onItemClick = { viableViewModel.onStopSelected(it) }
            )
        }
    }
}

@Composable
fun MainScreenLandscape(viableViewModel: ViableViewModel) {
    val context = LocalContext.current
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
    Scaffold {
        Row(modifier = Modifier.padding(it)) {
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
            GoogleMap(
                modifier = Modifier.weight(2f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    minZoomPreference = 5f,
                    maxZoomPreference = 15f,
                    mapStyleOptions = if (isSystemInDarkTheme()) {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_mode)
                    }
                    else {
                        null
                    },
                ),
            ) {
                selectedTrain?.let { train ->
                    Polyline(
                        endCap = RoundCap(),
                        startCap = RoundCap(),
                        jointType = JointType.ROUND,
                        points = routeLine.map { LatLng(it.lat, it.lon) },
                        color = colorResource(id = R.color.primary_colour),
                    )
                    train.location?.let {
                        Marker(
                            state = MarkerState(position = LatLng(it.lat, it.lon)),
                            title = selectedTrain.toString(),
                            icon = BitmapDescriptorFactory.fromAsset("train.png"),
                            snippet = if (!train.departed) {
                                stringResource(id = R.string.departed)
                            }
                            else if (train.arrived) {
                                stringResource(id = R.string.arrived)
                            }
                            else {
                                stringResource(
                                    id = R.string.next_stop, train.nextStop?.name ?: "",
                                    Html.fromHtml(
                                        train.nextStop?.eta,
                                        Html.FROM_HTML_MODE_LEGACY
                                    ),
                                )
                            },
                        )
                    }
                }
                selectedStation?.let {
                    Marker(
                        icon = BitmapDescriptorFactory.fromAsset("station.png"),
                        state = MarkerState(position = LatLng(it.lat, it.lon)),
                        title = it.name,
                    )
                }
            }
        }
    }
}