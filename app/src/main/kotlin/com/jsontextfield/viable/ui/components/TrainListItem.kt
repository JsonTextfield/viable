package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.model.Train

@Composable
fun TrainListItem(
    train: Train,
    isSelected: Boolean = false,
) {
    ListItem(
        modifier = Modifier.alpha(if (train.location != null) 1f else 0.5f),
        colors = ListItemDefaults.colors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else if (train.location != null) {
                MaterialTheme.colorScheme.surfaceContainerLow
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        headlineContent = {
            Text(train.toString())
        },
        supportingContent = {
            Text(
                if (train.nextStop != null) {
                    stringResource(
                        id = R.string.next_stop, train.nextStop?.name ?: "",
                        Html.fromHtml(train.nextStop?.eta ?: "", Html.FROM_HTML_MODE_LEGACY),
                    )
                }
                else if (train.arrived) {
                    stringResource(id = R.string.arrived)
                }
                else if (train.departed) {
                    stringResource(id = R.string.departed)
                }
                else {
                    ""
                },
            )
        },
    )
}

@Preview
@Composable
private fun TrainListItemPreview() {
    TrainListItem(
        train = Train(
            number = "45",
            headsign = "Ottawa -> Toronto",
            departed = true,
            arrived = true,
        )
    )
}