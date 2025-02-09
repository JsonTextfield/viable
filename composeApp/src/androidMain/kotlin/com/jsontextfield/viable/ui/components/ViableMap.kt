package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.LatLon
import com.jsontextfield.viable.data.model.Train
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.arrived
import viable.composeapp.generated.resources.departed
import viable.composeapp.generated.resources.next_stop

@OptIn(ExperimentalResourceApi::class)
@Composable
actual fun ViableMap(
    position: LatLon,
    modifier: Modifier,
    selectedTrain: Train?,
    selectedStation: Station?,
    routeLine: List<Shape>,
) {
    val cameraPositionState = rememberCameraPositionState()
    val context = LocalContext.current
    LaunchedEffect(position) {
        cameraPositionState.move(CameraUpdateFactory.newLatLng(LatLng(position.lat, position.lon)))
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            minZoomPreference = 5f,
            maxZoomPreference = 15f,
            mapStyleOptions = if (isSystemInDarkTheme()) {
                MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_mode)
            } else {
                null
            },
        ),
        uiSettings = MapUiSettings()
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
                        stringResource(Res.string.departed)
                    } else if (train.arrived) {
                        stringResource(Res.string.arrived)
                    } else {
                        stringResource(
                            Res.string.next_stop,
                            train.nextStop?.name ?: "",
                            train.nextStop?.eta ?: "",
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