package com.jsontextfield.viable.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics

@Composable
fun CountdownTimer(
    timeRemaining: Int,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = timeRemaining.toFloat() / 30_000,
        animationSpec = tween(
            durationMillis = if (timeRemaining == 30_000) 100 else 1100,
            easing = LinearEasing
        )
    )
    Box(modifier = modifier.clearAndSetSemantics {  }, contentAlignment = Alignment.Center) {
        CircularProgressIndicator(progress = { progress },)
        Text((timeRemaining / 1000).toString())
    }
}