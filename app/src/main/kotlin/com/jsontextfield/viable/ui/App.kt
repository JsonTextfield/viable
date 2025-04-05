package com.jsontextfield.viable.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.jsontextfield.viable.ui.components.MainScreen
import com.jsontextfield.viable.ui.theme.ViableTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel = koinViewModel<ViableViewModel>()
    val state by viewModel.viableState.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    ViableTheme {
        MainScreen(
            viableState = state,
            isPortrait = getScreenWidthDp() < 900.dp,
            onTrainSelected = viewModel::onTrainSelected,
            onStopSelected = viewModel::onStopSelected,
            timeRemaining = timeRemaining,
        )
    }
}