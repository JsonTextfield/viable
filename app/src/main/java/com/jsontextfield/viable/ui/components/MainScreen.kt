package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
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
import com.jsontextfield.viable.data.entities.Station
import com.jsontextfield.viable.ui.ViableViewModel
import kotlinx.coroutines.delay
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viableViewModel: ViableViewModel) {
    val context = LocalContext.current
    val trains by viableViewModel.trains.collectAsState()
    val selectedTrain by viableViewModel.selectedTrain.collectAsState()
    var routeLine by remember { mutableStateOf(listOf<LatLng>()) }
    var selectedStation by remember { mutableStateOf<Station?>(null) }
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
            routeLine = viableViewModel.getLine(train).map { shape ->
                LatLng(shape.lat, shape.lon)
            }
            train.nextStop?.let { stop ->
                selectedStation = viableViewModel.getStation(stop)
            }
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
                    ) {
                        viableViewModel.onTrainSelected(it)
                    }
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
                        points = routeLine,
                        color = colorResource(id = R.color.primary_colour),
                    )
                    train.location?.let {
                        Marker(
                            state = MarkerState(position = LatLng(it.lat, it.lon)),
                            title = selectedTrain.toString(),
                            icon = BitmapDescriptorFactory.fromAsset("train_24dp_FILL1_wght300_GRAD0_opsz24.png"),
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
                        state = MarkerState(position = LatLng(it.lat, it.lon)),
                        title = it.name,
                        icon = BitmapDescriptorFactory.fromAsset("things_to_do_24dp_FILL1_wght300_GRAD0_opsz24.png"),
                    )
                }
            }
            StopsList(
                stops = selectedTrain?.stops ?: emptyList(),
                modifier = Modifier.weight(.3f),
                listState = listState,
            )
        }
    }
}