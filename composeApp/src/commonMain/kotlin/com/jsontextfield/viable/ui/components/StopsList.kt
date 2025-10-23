package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Stop

@Composable
fun StopsList(
    modifier: Modifier = Modifier,
    selectedStation: Station? = null,
    stops: List<Stop> = emptyList(),
    listState: LazyListState = rememberLazyListState(),
    onItemClick: (Stop) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.semantics {
            collectionInfo = CollectionInfo(
                rowCount = stops.size,
                columnCount = 1
            )
        },
        state = listState,
        contentPadding = PaddingValues(
            bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
        )
    ) {
        itemsIndexed(stops, key = { _, stop -> stop.id }) { index, stop ->
            StopListItem(
                stop,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (stop.id == selectedStation?.code) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    )
                    .alpha(if (stop.eta != "ARR") 1f else 0.5f)
                    .clickable { onItemClick(stop) }
                    .padding(12.dp)
                    .semantics {
                        collectionItemInfo = CollectionItemInfo(
                            rowIndex = index,
                            columnIndex = 1,
                            rowSpan = 1,
                            columnSpan = 1,
                        )
                    }
                    .animateItem(),
            )
        }
    }
}