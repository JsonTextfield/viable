package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import org.apache.commons.text.StringEscapeUtils

@Composable
fun ViableMap(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    selectedTrain: Train? = null,
    selectedStation: Station? = null,
    routeLine: List<Shape> = emptyList(),
) {
    val context = LocalContext.current
    GoogleMap(
        modifier = modifier,
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
        contentPadding = WindowInsets.safeDrawing.asPaddingValues()
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
                            StringEscapeUtils.unescapeHtml4(train.nextStop?.eta ?: ""),
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