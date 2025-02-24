package com.jsontextfield.viable.ui.components

//import android.text.Html
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.jsontextfield.viable.data.model.Train
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
        modifier = Modifier.alpha(if (train.location != null) 1f else 0.5f),
        headlineContent = {
            SelectText(
                train.toString(),
                MaterialTheme.colorScheme.onSurface,
                isSelected
            )
        },
        supportingContent = {
            SelectText(
                if (train.nextStop != null) {
                    stringResource(
                        Res.string.next_stop,
                        train.nextStop?.name ?: "",
                        train.nextStop?.eta ?: "",
                    )
                } else if (train.arrived) {
                    stringResource(Res.string.arrived)
                } else if (train.departed) {
                    stringResource(Res.string.departed)
                } else {
                    ""
                },
                ListItemDefaults.colors().supportingTextColor,
                isSelected
            )
        },
    )
}