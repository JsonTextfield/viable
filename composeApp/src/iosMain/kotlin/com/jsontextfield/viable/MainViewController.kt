package com.jsontextfield.viable

import androidx.compose.ui.window.ComposeUIViewController
import com.jsontextfield.viable.data.database.entities.Shape
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.ui.App
import platform.UIKit.UIViewController

fun MainViewController(mapUIViewController: (Boolean, Boolean, Train?, Station?, List<Shape>) -> UIViewController) = ComposeUIViewController {
    mapViewController = mapUIViewController
    App()
}

lateinit var mapViewController: (Boolean, Boolean, Train?, Station?, List<Shape>) -> UIViewController

