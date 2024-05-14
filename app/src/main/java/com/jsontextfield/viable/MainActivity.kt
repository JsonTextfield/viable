package com.jsontextfield.viable

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.entities.Train
import com.jsontextfield.viable.ui.theme.ViableTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private fun downloadData(callback: (List<Train>) -> Unit = {}) {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val jsonObjectRequest = JsonObjectRequest(url, { response ->
            Log.e("RESPONSE", "downloaded data ${System.currentTimeMillis()}")
            val trains = response.keys().asSequence().map {
                response.getJSONObject(it).put("number", it)
                Train.fromJson(response.getJSONObject(it))
            }.sortedWith { train1, train2 ->
                train1.number.split(" ").first()
                    .compareTo(train2.number.split(" ").first())
            }.toList()
            callback(trains)
        }, {
            callback(ArrayList())
        })
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViableTheme {
                val context = LocalContext.current
                var selectedTrain by remember { mutableStateOf<Train?>(null) }
                var trains by remember { mutableStateOf(ArrayList<Train>()) }
                val cameraPositionState = rememberCameraPositionState()

                val callback: (List<Train>) -> Unit = {
                    trains = ArrayList(it)
                    selectedTrain = trains.find { train ->
                        train.number == selectedTrain?.number
                    } ?: trains.firstOrNull()
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
                                Log.e("TRAIN", "selected train $selectedTrain")
                                TrainComboBox(
                                    items = trains,
                                    selectedItem = selectedTrain,
                                ) {
                                    selectedTrain = it
                                    cameraPositionState.move(
                                        CameraUpdateFactory.newLatLng(
                                            selectedTrain!!.location!!.toLatLng()
                                        )
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    enabled = false,
                                    onClick = { downloadData { callback(it) } },
                                ) {
                                    Icon(Icons.Rounded.Refresh, null)
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
                            selectedTrain?.location?.toLatLng()?.let {
                                Marker(
                                    state = MarkerState(position = it),
                                    title = selectedTrain?.number,
                                    snippet = selectedTrain?.status,
                                )
                            }
                        }
                        LazyColumn(modifier = Modifier.weight(.3f)) {
                            items(selectedTrain?.stops ?: emptyList()) { stop ->
                                ListItem(
                                    modifier = Modifier.alpha(if (stop.eta != "ARR") 1f else 0.5f),
                                    headlineContent = { Text(stop.name) },
                                    supportingContent = {
                                        Text(
                                            if (stop.eta == "ARR") {
                                                "Departed"
                                            }
                                            else {
                                                "Arrives in ${
                                                    Html.fromHtml(
                                                        stop.eta,
                                                        Html.FROM_HTML_MODE_LEGACY
                                                    )
                                                }"
                                            }
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}