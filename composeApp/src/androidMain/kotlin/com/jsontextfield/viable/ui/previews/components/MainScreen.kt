package com.jsontextfield.viable.ui.previews.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jsontextfield.viable.ui.ViableState
import com.jsontextfield.viable.ui.components.MainScreen

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        viableState = ViableState(),
        timeRemaining = 14000,
        onTrainSelected = { },
        onStopSelected = { },
        isPortrait = true
    )
}