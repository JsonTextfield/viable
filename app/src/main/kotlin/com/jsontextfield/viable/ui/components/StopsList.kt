package com.jsontextfield.viable.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CollectionInfo
import androidx.compose.ui.semantics.CollectionItemInfo
import androidx.compose.ui.semantics.collectionInfo
import androidx.compose.ui.semantics.collectionItemInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.jsontextfield.viable.R
import com.jsontextfield.viable.data.database.entities.Station
import com.jsontextfield.viable.data.model.Stop
import org.apache.commons.text.StringEscapeUtils

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
            ListItem(
                modifier = Modifier
                    .alpha(if (stop.eta != "ARR") 1f else 0.5f)
                    .clickable { onItemClick(stop) }
                    .semantics {
                        collectionItemInfo = CollectionItemInfo(
                            rowIndex = index,
                            columnIndex = 1,
                            rowSpan = 1,
                            columnSpan = 1,
                        )
                    }
                    .animateItem(),
                headlineContent = {
                    Text(
                        stop.name,

                        modifier = Modifier.padding(
                            start = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateStartPadding(LayoutDirection.Ltr),
                        )
                    )
                },
                supportingContent = {
                    Text(
                        if (stop.eta == "ARR") {
                            stringResource(id = R.string.departed)
                        } else {
                            stringResource(
                                id = R.string.arrives_in,
                                StringEscapeUtils.unescapeHtml4(stop.eta),
                            )
                        },
                        modifier = Modifier.padding(
                            start = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateStartPadding(LayoutDirection.Ltr)
                        ),
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = if (stop.id == selectedStation?.code) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
            )
        }
    }
}

@Preview
@Composable
private fun StopListPreview() {
    StopsList(
        stops = (1 until 10).map {
            Stop("Stop $it", "${it * 7}m")
        },
    )
}