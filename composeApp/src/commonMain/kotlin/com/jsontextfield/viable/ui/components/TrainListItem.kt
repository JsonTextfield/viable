package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.replaceHtmlEntities
import org.jetbrains.compose.resources.stringResource
import viable.composeapp.generated.resources.Res
import viable.composeapp.generated.resources.arrived
import viable.composeapp.generated.resources.departed
import viable.composeapp.generated.resources.next_stop

@Composable
fun TrainListItem(
    train: Train,
    isSelected: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else if (train.hasLocation) {
                    MaterialTheme.colorScheme.surfaceContainerLow
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .alpha(if (train.isEnabled) 1f else 0.5f)
            .padding(12.dp)
    ) {
        Text(train.name)
        Text(
            when {
                train.arrived -> {
                    stringResource(Res.string.arrived)
                }

                train.nextStop != null -> {
                    stringResource(
                        Res.string.next_stop, train.nextStop?.name.orEmpty(),
                        train.nextStop?.eta.orEmpty().replaceHtmlEntities(),
                    )
                }

                train.departed -> {
                    stringResource(Res.string.departed)
                }

                else -> {
                    ""
                }
            },
            style = MaterialTheme.typography.labelMedium,
        )
    }
}