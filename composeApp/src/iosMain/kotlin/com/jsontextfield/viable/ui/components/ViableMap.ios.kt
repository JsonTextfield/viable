package com.jsontextfield.viable.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.Google_Maps_iOS_Utils.GMSCameraPosition
import cocoapods.Google_Maps_iOS_Utils.GMSCameraUpdate
import cocoapods.Google_Maps_iOS_Utils.GMSMapView
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ViableMap(
    modifier: Modifier,
    shouldMoveCamera: Boolean,
    selectedTrain: Train?,
    selectedStation: Station?,
    routeLine: List<Shape>,
    isPortrait: Boolean
) {
    val mapView = remember { GMSMapView() }

    val cameraPosition = GMSCameraPosition.cameraWithLatitude(43.64, -79.78, 6.0F)
    val cameraUpdate = GMSCameraUpdate.setCamera(cameraPosition)
    mapView.moveCamera(cameraUpdate)

    UIKitView(
        modifier = modifier,
        factory = { mapView }
    )
}
