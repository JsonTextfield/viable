package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.GoogleMaps.GMSCameraPosition
import cocoapods.GoogleMaps.GMSMapStyle
import cocoapods.GoogleMaps.GMSMapView
import cocoapods.GoogleMaps.GMSMarker
import cocoapods.GoogleMaps.GMSMutablePath
import cocoapods.GoogleMaps.GMSPolyline
import cocoapods.GoogleMaps.animateToCameraPosition
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.database.toNSData
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.replaceHtmlEntities
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.arrived
import viable.composeapp.generated.resources.departed
import viable.composeapp.generated.resources.next_stop

@OptIn(ExperimentalForeignApi::class, ExperimentalResourceApi::class)
@Composable
actual fun ViableMap(
    modifier: Modifier,
    shouldMoveCamera: Boolean,
    selectedTrain: Train?,
    selectedStation: Station?,
    routeLine: List<Shape>,
    isPortrait: Boolean
) {

    val (red, green, blue, alpha) = MaterialTheme.colorScheme.primary
    val darkTheme = isSystemInDarkTheme()
    val mapView = remember {
        GMSMapView().apply {
            setMinZoom(5f, 15f)
            if (darkTheme) {
                runBlocking {
                    setMapStyle(
                        GMSMapStyle.styleWithJSONString(
                            Res.readBytes("files/dark_mode.json").decodeToString(), null
                        )
                    )
                }
            }
        }
    }
    val trainMarker = remember {
        GMSMarker().apply {
            runBlocking {
                val icon = UIImage(Res.readBytes("files/train.png").toNSData())
                setIcon(icon)
            }
            setMap(mapView)
        }
    }
    val stationMarker = remember {
        GMSMarker().apply {
            runBlocking {
                val icon = UIImage(Res.readBytes("files/station.png").toNSData())
                setIcon(icon)
            }
            setMap(mapView)
        }
    }
    val route = remember {
        GMSPolyline().apply {
            setStrokeWidth(5.0)
            setStrokeColor(
                UIColor(
                    red = red.toDouble(),
                    green = green.toDouble(),
                    blue = blue.toDouble(),
                    alpha = alpha.toDouble()
                )
            )
            setMap(mapView)
        }
    }

    LaunchedEffect(selectedTrain, selectedStation) {
        selectedTrain?.let { train ->
            if (train.hasLocation) {
                if (shouldMoveCamera) {
                    val cameraUpdate = GMSCameraPosition.cameraWithLatitude(
                        latitude = train.lat!!,
                        longitude = train.lng!!,
                        zoom = 10f,
                    )
                    mapView.animateToCameraPosition(cameraUpdate)
                }

                trainMarker.apply {
                    setPosition(
                        CLLocationCoordinate2DMake(
                            latitude = train.lat!!,
                            longitude = train.lng!!
                        )
                    )
                    setTitle(selectedTrain.name)
                    setSnippet(
                        if (!train.departed) {
                            getString(Res.string.departed)
                        } else if (train.arrived) {
                            getString(Res.string.arrived)
                        } else {
                            getString(
                                Res.string.next_stop, train.nextStop?.name ?: "",
                                train.nextStop?.eta.orEmpty().replaceHtmlEntities(),
                            )
                        },
                    )
                }

                route.apply {
                    val path = GMSMutablePath()
                    routeLine.forEach { shape ->
                        path.addLatitude(shape.lat, shape.lon)
                    }
                    setPath(path)
                }
            }
        }
        selectedStation?.let {
            stationMarker.apply {
                setPosition(
                    CLLocationCoordinate2DMake(
                        latitude = it.lat,
                        longitude = it.lon
                    )
                )
                setTitle(it.name)
            }
        }
    }

    UIKitView(
        modifier = modifier.fillMaxSize(),
        factory = { mapView },
    )
}