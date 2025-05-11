package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.jsontextfield.viable.data.model.Train

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainComboBox(
    items: List<Train> = emptyList(),
    selectedItem: Train? = null,
    onItemSelected: (Train) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(
            start = WindowInsets.safeDrawing.asPaddingValues()
                .calculateStartPadding(LayoutDirection.Ltr),
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    selectedItem?.name.orEmpty(),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            },
            trailingContent = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { train ->
                DropdownMenuItem(
                    contentPadding = PaddingValues(0.dp),
                    enabled = train.location != null,
                    text = { TrainListItem(train, selectedItem == train) },
                    onClick = {
                        onItemSelected(train)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrainComboBoxPreview() {
    TrainComboBox(
        selectedItem = Train(
            number = "45",
            from = "Ottawa",
            to = "Toronto",
        )
    )
}