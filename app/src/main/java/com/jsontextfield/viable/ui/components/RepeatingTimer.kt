package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun RepeatingTimer(
    totalMilliseconds: Long,
    modifier: Modifier = Modifier,
    onTimerExpired: () -> Unit = {},
) {
    var remainingMilliseconds by remember { mutableLongStateOf(totalMilliseconds) }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(20)
                remainingMilliseconds -= 20L
                if (remainingMilliseconds <= 0L) {
                    onTimerExpired()
                    remainingMilliseconds = totalMilliseconds
                }
            }
        }
        CircularProgressIndicator(
            progress = { remainingMilliseconds.toFloat() / totalMilliseconds.toFloat() },
        )
        Text(text = (remainingMilliseconds / 1000).toInt().toString())
    }
}