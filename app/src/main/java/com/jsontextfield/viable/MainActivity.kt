package com.jsontextfield.viable

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.ui.theme.ViableTheme
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    private fun downloadData(callback: (List<Train>) -> Unit = {}) {
        val url = "https://tsimobile.viarail.ca/data/allData.json"
        val jsonObjectRequest = JsonObjectRequest(url, { response ->
            // Log.e("RESPONSE", response.toString())
            val trains = response.keys().asSequence().map {
                Train.fromJson(response.getJSONObject(it))
            }.toList()
            callback(trains)
        }, {
            callback(ArrayList())
        })
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViableTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    var selectedTrain by remember { mutableStateOf<Train?>(null) }
                    var trains by remember { mutableStateOf(ArrayList<Train>()) }

                    LaunchedEffect(true) {
                        val url = "https://tsimobile.viarail.ca/data/allData.json"
                        val jsonObjectRequest = JsonObjectRequest(url, { response ->
                            trains = ArrayList(response.keys().asSequence().map {
                                response.getJSONObject(it).put("number", it)
                                Train.fromJson(response.getJSONObject(it))
                            }.sortedWith { train1, train2 ->
                                train1.number.split(" ").first()
                                    .compareTo(train2.number.split(" ").first())
                            }.toList())
                        }, {
                            trains = ArrayList()
                        })
                        Volley.newRequestQueue(context).add(jsonObjectRequest)
                    }

                    Column(modifier = Modifier.padding(innerPadding)) {
                        LazyColumn(modifier = Modifier.weight(.2f)) {
                            items(trains) { train ->
                                ListItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = train.location != null) {
                                            selectedTrain = train
                                        },
                                    headlineContent = { Text(train.number) },
                                    supportingContent = { Text(train.status) },
                                )
                            }
                        }
                        val cameraPositionState = CameraPositionState(
                            CameraPosition.fromLatLngZoom(
                                (selectedTrain?.location?.toLatLng() ?: LatLng(0.0, 0.0)), 12f
                            )
                        )
                        GoogleMap(
                            modifier = Modifier.weight(.5f),
                            cameraPositionState = cameraPositionState,
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
                                    modifier = Modifier.fillMaxWidth(),
                                    headlineContent = {
                                        Text(stop.name)
                                    },
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ViableTheme {
        Greeting("Android")
    }
}