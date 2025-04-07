package com.jsontextfield.viable.ui.components

import android.text.Html
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
        )
    ) {
        items(stops) { stop ->
            ListItem(
                modifier = Modifier
                    .alpha(if (stop.eta != "ARR") 1f else 0.5f)
                    .clickable { onItemClick(stop) },
                headlineContent = {
                    Text(stop.name)
                },
                supportingContent = {
                    Text(
                        if (stop.eta == "ARR") {
                            stringResource(id = R.string.departed)
                        }
                        else {
                            stringResource(
                                id = R.string.arrives_in,
                                Html.fromHtml(
                                    stop.eta,
                                    Html.FROM_HTML_MODE_LEGACY
                                ),
                            )
                        },
                    )
                },
                colors = ListItemDefaults.colors(
                    containerColor = if (stop.id == selectedStation?.code) {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                    else {
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