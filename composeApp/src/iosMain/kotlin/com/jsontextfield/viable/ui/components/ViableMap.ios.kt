package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.mapViewController

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
    var _isPortrait by remember { mutableStateOf(isPortrait) }
    var _shouldMoveCamera by remember { mutableStateOf(shouldMoveCamera) }
    var _selectedTrain by remember { mutableStateOf(selectedTrain) }
    var _selectedStation by remember { mutableStateOf(selectedStation) }
    var _routeLine by remember { mutableStateOf(routeLine) }
    val view = remember {
        mapViewController(
            _isPortrait,
            _shouldMoveCamera,
            _selectedTrain,
            _selectedStation,
            _routeLine,
            listOf(red, green, blue, alpha),
        )
    }
    UIKitViewController(
        factory = { view },
        modifier = Modifier.fillMaxSize(),
        update = { viewController ->
            println("updating from ViableMap")
            _isPortrait = isPortrait
            _shouldMoveCamera = shouldMoveCamera
            _selectedTrain = selectedTrain
            _selectedStation = selectedStation
            _routeLine = routeLine
        },
    )
}
