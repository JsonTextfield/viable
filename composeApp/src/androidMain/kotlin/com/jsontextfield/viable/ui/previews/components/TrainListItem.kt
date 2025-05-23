package com.jsontextfield.viable.ui.previews.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.ui.components.TrainListItem

@Preview
@Composable
private fun TrainListItemPreview() {
    TrainListItem(
        train = Train(
            number = "45",
            from = "Ottawa",
            to = "Toronto",
            departed = true,
            arrived = true,
        )
    )
}