package com.jsontextfield.viable.ui.components

import android.text.Html
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.R
import com.jsontextfield.viable.entities.Train
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    var selectedTrain by remember { mutableStateOf<Train?>(null) }
    var trains by remember { mutableStateOf(ArrayList<Train>()) }
    val cameraPositionState = rememberCameraPositionState()

    fun downloadData(callback: (List<Train>) -> Unit = {}) {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val jsonObjectRequest = JsonObjectRequest(url, { response ->
            val data = response.keys().asSequence().map {
                response.getJSONObject(it).put("number", it)
                Train.fromJson(response.getJSONObject(it))
            }.sortedBy { train ->
                train.number.split(" ").first().toInt()
            }.toList()
            callback(data)
        }, {
            callback(ArrayList())
        })
        Volley.newRequestQueue(context).add(jsonObjectRequest)
    }

    fun callback(data: List<Train>) {
        trains = ArrayList(data)
        selectedTrain = trains.find { train ->
            train.number == selectedTrain?.number
        } ?: trains.firstOrNull()

        Log.e("SelectedTrain", "$selectedTrain")
        selectedTrain?.let { train ->
            train.location?.let { latLon ->
                cameraPositionState.move(CameraUpdateFactory.newLatLng(latLon.toLatLng()))
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TrainComboBox(items = trains, selectedItem = selectedTrain) {
                        selectedTrain = it
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLng(
                                selectedTrain!!.location!!.toLatLng()
                            )
                        )
                    }
                },
            )
        },
    ) { innerPadding ->

        LaunchedEffect(true) {
            while (true) {
                downloadData { callback(it) }
                delay(30_000)
            }
        }

        Column(modifier = Modifier.padding(innerPadding)) {
            GoogleMap(
                modifier = Modifier.weight(.5f),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    minZoomPreference = 5f,
                    mapStyleOptions = if (isSystemInDarkTheme()) {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_mode)
                    }
                    else {
                        null
                    },
                ),
            ) {
                selectedTrain?.let { train ->
                    train.location?.let {
                        Marker(
                            state = MarkerState(position = it.toLatLng()),
                            title = selectedTrain?.number,
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
            }
            StopsList(stops = selectedTrain?.stops ?: emptyList(), modifier = Modifier.weight(.3f))
        }
    }
}