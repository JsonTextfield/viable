package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.jsontextfield.viable.R
import com.jsontextfield.viable.entities.Train

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainComboBox(
    items: List<Train>,
    selectedItem: Train?,
    onItemSelected: (Train) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Text(
                    selectedItem?.toString() ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            trailingContent = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { train ->
                DropdownMenuItem(
                    enabled = train.location != null,
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        ListItem(
                            modifier = Modifier.alpha(if (train.location != null) 1f else 0.5f),
                            headlineContent = {
                                Text(train.toString())
                            },
                            supportingContent = {
                                Text(
                                    if (!train.departed) {
                                        stringResource(id = R.string.departed)
                                    }
                                    else if (train.arrived) {
                                        stringResource(id = R.string.arrived)
                                    }
                                    else {
                                        stringResource(
                                            id = R.string.next_stop, train.nextStop?.name ?: "",
                                            Html.fromHtml(
                                                train.nextStop?.eta ?: "",
                                                Html.FROM_HTML_MODE_LEGACY
                                            ),
                                        )
                                    },
                                )
                            },
                        )
                    },
                    onClick = {
                        onItemSelected(train)
                        expanded = false
                    }
                )
            }
        }
    }
}