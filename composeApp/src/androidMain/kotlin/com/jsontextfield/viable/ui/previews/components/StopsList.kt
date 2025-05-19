package com.jsontextfield.viable.ui.previews.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsontextfield.viable.data.model.Stop
import com.jsontextfield.viable.ui.components.StopsList

@Preview
@Composable
private fun StopListPreview() {
    StopsList(
        stops = (1 until 10).map {
            Stop(id = "$it", name = "Stop $it", "${it * 7}m")
        },
    )
}