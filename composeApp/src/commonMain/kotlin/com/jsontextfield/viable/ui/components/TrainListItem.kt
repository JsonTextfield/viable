package com.jsontextfield.viable.ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.jsontextfield.viable.data.model.Train
import com.jsontextfield.viable.data.model.replaceHtmlEntities
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
    ListItem(
        modifier = Modifier.alpha(if (train.isEnabled) 1f else 0.5f),
        colors = ListItemDefaults.colors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else if (train.hasLocation) {
                MaterialTheme.colorScheme.surfaceContainerLow
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        headlineContent = {
            Text(train.name)
        },
        supportingContent = {
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
                }
            )
        },
    )
}