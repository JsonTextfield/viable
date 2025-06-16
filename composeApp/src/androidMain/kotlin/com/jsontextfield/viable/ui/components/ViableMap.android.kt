package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import org.apache.commons.text.StringEscapeUtils
import org.jetbrains.compose.resources.stringResource
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.arrived
import viable.composeapp.generated.resources.departed
import viable.composeapp.generated.resources.next_stop

@Composable
actual fun ViableMap(
    modifier: Modifier,
    shouldMoveCamera: Boolean,
    selectedTrain: Train?,
    selectedStation: Station?,
    routeLine: List<Shape>,
    isPortrait: Boolean
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(selectedTrain) {
        if (shouldMoveCamera && selectedTrain?.hasLocation == true) {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        selectedTrain.lat!!,
                        selectedTrain.lng!!
                    )
                )
            )
        }
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
            latLngBoundsForCameraTarget = if (routeLine.isNotEmpty()) {
                LatLngBounds.builder().run {
                    routeLine.forEach { shape ->
                        include(LatLng(shape.lat, shape.lon))
                    }
                    build()
                }
            } else {
                null
            }
        ),
        contentPadding = PaddingValues(
            bottom = if (isPortrait) 0.dp else WindowInsets.safeDrawing.asPaddingValues()
                .calculateBottomPadding(),
            end = WindowInsets.safeDrawing.asPaddingValues()
                .calculateEndPadding(LayoutDirection.Ltr)
        )
    ) {
        selectedTrain?.let { train ->
            Polyline(
                endCap = RoundCap(),
                startCap = RoundCap(),
                jointType = JointType.ROUND,
                points = routeLine.map { LatLng(it.lat, it.lon) },
                color = MaterialTheme.colorScheme.primary,
            )
            if (train.hasLocation) {
                Marker(
                    state = MarkerState(position = LatLng(train.lat!!, train.lng!!)),
                    title = selectedTrain.name,
                    icon = BitmapDescriptorFactory.fromAsset("train.png"),
                    snippet = if (!train.departed) {
                        stringResource(Res.string.departed)
                    } else if (train.arrived) {
                        stringResource(Res.string.arrived)
                    } else {
                        stringResource(
                            Res.string.next_stop, train.nextStop?.name ?: "",
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
                title = selectedTrain?.stops?.firstOrNull { stop -> stop.id == it.code }?.name.orEmpty(),
            )
        }
    }
}