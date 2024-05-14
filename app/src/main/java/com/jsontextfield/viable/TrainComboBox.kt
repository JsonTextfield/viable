package com.jsontextfield.viable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
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
                    selectedItem?.number ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            trailingContent = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        // Dropdown menu with list of items
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
                            headlineContent = { Text(train.number) },
                            supportingContent = { Text(train.status) },
                            colors = ListItemDefaults.colors(),
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