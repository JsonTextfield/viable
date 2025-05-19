package com.jsontextfield.viable.ui.previews.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.ui.components.TrainComboBox

@Preview
@Composable
private fun TrainComboBoxPreview() {
    TrainComboBox(
        selectedItem = Train(
            number = "45",
            from = "Ottawa",
            to = "Toronto",
        )
    )
}